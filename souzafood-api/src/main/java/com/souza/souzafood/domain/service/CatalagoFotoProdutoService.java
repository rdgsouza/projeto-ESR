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
import com.souza.souzafood.domain.model.MediaTypes;
import com.souza.souzafood.domain.repository.FotoRepository;
import com.souza.souzafood.domain.repository.ProdutoRepository;
import com.souza.souzafood.domain.service.FotoStorageService.NovaFoto;

@Service
public class CatalagoFotoProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private FotoRepository fotoRepository;

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
				.contentType(foto.getContentType())
				.inputStream(dadosIns)
				.build();

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

	public FotoProduto buscaFotoPorNome(String nomeArquivo) {
	 return fotoRepository.findFotoProdutoByNomeArquivo(nomeArquivo)
				.orElseThrow(() -> new FotoProdutoNaoEncontradaException(nomeArquivo));
	}
	
	private InputStream verificaFotoSemExtensao(FotoProduto foto, InputStream dadosArquivo) throws IOException {

		InputStream byteArrayInputStream = new ByteArrayInputStream(dadosArquivo.readAllBytes());
		String mediaType = URLConnection.guessContentTypeFromStream(byteArrayInputStream);		
		
		if (fotoStorage.pegarExtensaoArquivo(foto.getNomeArquivo()) == "") {
		  MediaTypes mt = MediaTypes.PNG;
          String ext =  mt.retornaExtensao(mediaType);
			
			String nomeNovoArquivoPng = fotoStorage.gerarNovoNomeArquivo(foto.getNomeArquivo());
			foto.setNomeArquivo(nomeNovoArquivoPng.concat(ext));
			foto.setContentType(mediaType);
			
			return byteArrayInputStream;
		}
		
		return byteArrayInputStream;
	}
}