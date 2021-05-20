package com.souza.souzafood.domain.model;

import com.souza.souzafood.infrastructure.service.storage.StorageException;

public enum MediaTypes {

	PNG {
		@Override
		public String toString() {
			return "image/png";
		}
	},
	JPEG {
		@Override
		public String toString() {
			return "image/jpeg";
		}
	};

	public String retornaExtensao(String mediaType) {

		String mimePng = PNG.toString();
		String mimeJpeg = JPEG.toString();
		
		try {
			if (mimePng == mediaType || mimeJpeg == mediaType) {
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
		// OBS***: Customizar com uma resposta e um status melhor. Criar uma exception Storage na
		// classe ApiExceptionHandler
			throw new StorageException("A foto deve ser do tipo JPG ou PNG.", e);
		}
		
		if (PNG.toString() == mediaType) {
			return ".png";
		} else if (JPEG.toString() == mediaType) {
			return ".jpeg";
		} else {
			return "";
		}
	}
}