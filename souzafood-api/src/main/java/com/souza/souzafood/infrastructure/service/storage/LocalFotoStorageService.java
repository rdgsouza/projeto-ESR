package com.souza.souzafood.infrastructure.service.storage;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;

import com.souza.souzafood.core.storage.StorageProperties;
import com.souza.souzafood.domain.service.FotoStorageService;

//Aula: https://www.algaworks.com/aulas/2060/implementando-o-servico-de-armazenagem-de-fotos-no-disco-local
//@Service   //OBS: Para usar essa classe descomente a anotação @Service
public class LocalFotoStorageService implements FotoStorageService {

	@Autowired
	private StorageProperties storageProperties;

	@Override
	public void armazenar(NovaFoto novaFoto) {
		try {
			Path arquivoPath = getArquivoPath(novaFoto.getNomeArquivo());

			FileCopyUtils.copy(novaFoto.getInputStream(), Files.newOutputStream(arquivoPath));
		} catch (Exception e) {
			throw new StorageException("Não foi possível armazenar arquivo.", e);
		}
	}

	@Override
	public void remover(String nomeArquivo) {
		try {
			Path arquivoPath = getArquivoPath(nomeArquivo);

			Files.deleteIfExists(arquivoPath);
		} catch (Exception e) {
			throw new StorageException("Não foi possível excluir arquivo.", e);
		}
	}

	@Override
	public InputStream recuperar(String nomeArquivo) {

		try {
			Path arquivoPath = getArquivoPath(nomeArquivo);

			return Files.newInputStream(arquivoPath);
		} catch (Exception e) {
			throw new StorageException("Não foi possível recuperar arquivo.", e);
		}
	}

	public MediaType retornaTipoDeMidia(String nomeArquivo) {

		try {
			Path caminhoArquivo = getArquivoPath(nomeArquivo);

			MediaType mediaType = returnMediaType(nomeArquivo, caminhoArquivo);
			
			return mediaType;

		} catch (Exception e) {
			throw new StorageException("Não foi possível obter o tipo de mídia do arquivo.", e);
		}
	}

	private Path getArquivoPath(String nomeArquivo) {

		return storageProperties.getLocal().getDiretorioFotos().resolve(Path.of(nomeArquivo));
	}

}
