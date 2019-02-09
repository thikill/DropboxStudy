package com.dropbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;

public class App {
	private static final String ACCESS_TOKEN = "L2wW9-hSeBoAAAAAAAKnV_-mKXHMwjF0XHJjYqwskg7Ir6oFJXsqSqjDn3QLSB2l";

	public static void main(String args[]) throws Exception {
		// Zip file
		// zipFile("D:\\data\\cleandata\\1\\", "1","html", "D:\\data\\cleandata\\1\\");
		// Create client
		DbxClientV2 client = getClient();
		// Download file
		downloadFile(client, "/1/1", "1", "D:");
		// Unzip file
		unzipFile("D:/1", "D:/");
	}

	public static DbxClientV2 getClient() {
		DbxRequestConfig config = DbxRequestConfig.newBuilder("").build();
		DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
		return client;
	}

	public static void downloadFile(DbxClientV2 client, String srcDir, String fileName, String destDir)
			throws Exception {

		DbxDownloader<FileMetadata> downloader = client.files().download(srcDir + "/" + fileName);
		FileOutputStream out = new FileOutputStream(destDir + File.separator + fileName);
		downloader.download(out);
		out.close();
	}

	public static void unzipFile(String fileZip, String destDirPath) throws Exception {
		byte[] buffer = new byte[1024];
		File destDir = new File(destDirPath);
		ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
		ZipEntry zipEntry = zis.getNextEntry();
		while (zipEntry != null) {
			File newFile = newFile(destDir, zipEntry);
			FileOutputStream fos = new FileOutputStream(newFile);
			int len;
			while ((len = zis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
			fos.close();
			zipEntry = zis.getNextEntry();
		}
		zis.closeEntry();
		zis.close();
	}

	public static void zipFile(String srcDir, String fileName, String fileExt, String destDir) throws Exception {
		FileOutputStream fos = new FileOutputStream(destDir + File.separator + fileName);
		ZipOutputStream zipOut = new ZipOutputStream(fos);
		File fileToZip = new File(srcDir + File.separator + fileName + "." + fileExt);
		FileInputStream fis = new FileInputStream(fileToZip);
		ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
		zipOut.putNextEntry(zipEntry);
		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zipOut.write(bytes, 0, length);
		}
		zipOut.close();
		fis.close();
		fos.close();

	}

	private static File newFile(File destinationDir, ZipEntry zipEntry) throws Exception {
		File destFile = new File(destinationDir, zipEntry.getName());
		return destFile;
	}
}