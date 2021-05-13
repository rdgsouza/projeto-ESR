package com.souza.souzafood.domain.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.souza.souzafood.domain.exception.FotoProdutoNaoEncontradaException;
import com.souza.souzafood.domain.model.FotoProduto;
import com.souza.souzafood.domain.repository.ProdutoRepository;
import com.souza.souzafood.domain.service.FotoStorageService.NovaFoto;
import com.souza.souzafood.infrastructure.service.storage.StorageException;

@Service
public class CatalagoFotoProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private FotoStorageService fotoStorage;

	@Transactional
	public FotoProduto salvar(FotoProduto foto, InputStream dadosArquivo) throws IOException {

		Long restauranteId = foto.getRestauranteId();
		Long produtoId = foto.getProduto().getId();
		String nomeArquivoExistente = null;
		String nomeNovoArquivo = fotoStorage.gerarNovoNomeArquivo(foto.getNomeArquivo());
		
		foto.setNomeArquivo(nomeNovoArquivo);

		InputStream dadosIns = verificaFotoSemExtensao(foto, dadosArquivo);

		Optional<FotoProduto> fotoExistente = produtoRepository
				.findFotoById(restauranteId, produtoId);

		if (fotoExistente.isPresent()) {
			nomeArquivoExistente = fotoExistente.get().getNomeArquivo();
			produtoRepository.delete(fotoExistente.get());
		}
		
		foto = produtoRepository.save(foto);
		produtoRepository.flush();

		NovaFoto novaFoto = NovaFoto.builder()
				.nomeArquivo(foto.getNomeArquivo())
				.inputStream(dadosIns).build();

		fotoStorage.substituir(nomeArquivoExistente, novaFoto);

		return foto;
	}

	@Transactional
	public void excluir(Long restauranteId, Long produtoId) {
		FotoProduto fotoProduto = buscarOuFalhar(restauranteId, produtoId);
		produtoRepository.delete(fotoProduto);
		produtoRepository.flush();
		fotoStorage.remover(fotoProduto.getNomeArquivo());
	}

	public FotoProduto buscarOuFalhar(Long restauranteId, Long produtoId) {
		return produtoRepository.findFotoById(restauranteId, produtoId)
				.orElseThrow(() -> new FotoProdutoNaoEncontradaException(restauranteId, produtoId));
	}

	public List<FotoProduto> buscarTodos(Long restauranteId) {
		return produtoRepository.findAllRestauranteById(restauranteId);
	}

	private InputStream verificaFotoSemExtensao(FotoProduto foto, InputStream dadosArquivo) throws IOException {

		byte[] bytes = dadosArquivo.readAllBytes();
		InputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
		String mediaType = URLConnection.guessContentTypeFromStream(byteArrayInputStream);

		if (fotoStorage.pegarExtensaoArquivo(foto.getNomeArquivo()) == "") {
			if (mediaType == "image/png") {
				String nomeNovoArquivoPng = fotoStorage.gerarNovoNomeArquivo(foto.getNomeArquivo());
				foto.setNomeArquivo(nomeNovoArquivoPng.concat("png"));
				foto.setContentType(mediaType);
				return byteArrayInputStream;
			}

			if (mediaType == "image/jpeg") {
				String nomeNovoArquivojpeg = fotoStorage.gerarNovoNomeArquivo(foto.getNomeArquivo());
				nomeNovoArquivojpeg = nomeNovoArquivojpeg.concat("jpeg");
				foto.setNomeArquivo(nomeNovoArquivojpeg);
				foto.setContentType(mediaType);
				return byteArrayInputStream;
			}

			try {
				if (mediaType != "image/png" || mediaType != "image/jpeg") {
					throw new Exception();
				}
			} catch (Exception e) {
			// OBS***: Customizar com uma resposta e um status melhor. Criar uma exception Storage na
			// classe ApiExceptionHandler
				throw new StorageException("A foto deve ser do tipo JPG ou PNG.", e);
			}
		}	

		return byteArrayInputStream;
	}
}