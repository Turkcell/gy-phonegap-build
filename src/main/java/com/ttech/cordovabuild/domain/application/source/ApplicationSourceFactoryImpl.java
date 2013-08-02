package com.ttech.cordovabuild.domain.application.source;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ttech.cordovabuild.domain.asset.AssetRepository;
import com.ttech.cordovabuild.infrastructure.archive.ArchiveExtractor;
import com.ttech.cordovabuild.infrastructure.git.GitRepository;

@Service
public class ApplicationSourceFactoryImpl implements ApplicationSourceFactory {
	@Autowired
	AssetRepository assetRepository;
	@Autowired
	ArchiveExtractor archiveExtractor;
	@Autowired
	GitRepository gitRepository;

	@Override
	public ApplicationSource createSource(String uri, Path localPath) {
		if (uri.startsWith("asset:")) {
			return new ArchiveSource(assetRepository.findByID(Long
					.parseLong(uri.substring(5))), archiveExtractor, localPath);
		} else {
			return new GitSource(uri, gitRepository, localPath);
		}
	}
}
