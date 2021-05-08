package com.souza.souzafood.infrastructure.service.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.souza.souzafood.domain.service.FotoStorageService;

//Aula: https://www.algaworks.com/aulas/2060/implementando-o-servico-de-armazenagem-de-fotos-no-disco-local
@Service
public class LocalFotoStorageService implements FotoStorageService {

	@Value("${souzafood.storage.local.diretorio-fotos}")
	private Path diretorioFotos; // O tipo da variavel não precisa ser String pode ser Path mesmo o Soring ja faz
									// a conversão pra gente

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

	@Override
	public MediaType retornaMediaType(String nomeArquivo) throws IOException {
		
		Path caminhoArquivo = getArquivoPath(nomeArquivo);
		String contentType = Files.probeContentType(Path.of(caminhoArquivo.toString()));		
        MediaType mediaType = MediaType.parseMediaType(contentType);
		
        return mediaType;
	}
	
	private Path getArquivoPath(String nomeArquivo) {

		return diretorioFotos.resolve(Path.of(nomeArquivo));
	}

}
