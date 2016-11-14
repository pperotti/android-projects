package com.pabloperotti.backup.in.the.cloud;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import android.util.Log;

import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Changes;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.Change;
import com.google.api.services.drive.model.ChangeList;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;

public class DriveUtils {

	public static final int BUFFER_SIZE = 4096;
	private static FileNameMap fileNameMap = URLConnection.getFileNameMap();

	public enum GoogleDriveMimeType {

		DOCUMENT("application/vnd.google-apps.document"), SPREADSHEET(
				"application/vnd.google-apps.spreadsheet"), FOLDER(
				"application/vnd.google-apps.folder");

		String mimeType = null;

		GoogleDriveMimeType(String mimeType) {
			this.mimeType = mimeType;
		}

		public String getMimeType() {
			return mimeType;
		}
	};

	/**
	 * Retrieve a list of File resources.
	 *
	 * @param service
	 *            Drive API service instance.
	 * @return List of File resources.
	 */
	public static List<File> retrieveAllFiles(Drive service) throws IOException {

		Log.i(MyApp.TAG, "DriveUtils.retrieveAllFiles()");

		List<File> result = new ArrayList<File>();
		Files.List request = service.files().list();
		request.setPageToken(MyApp.ACCOUNTINFO.getToken());

		do {
			FileList files = request.execute();
			result.addAll(files.getItems());
		} while (request.getPageToken() != null
				&& request.getPageToken().length() > 0);

		return result;
	}

	/**
	 * Create a folder with the specified name.
	 *
	 * @param folderName
	 */
	public static void createFolder(Drive service, String folderName) {

		Log.i(MyApp.TAG, "DriveUtils.createFolder()");

		try {
			File folder = new File();
			folder.setTitle(folderName);
			folder.setMimeType(DriveUtils.GoogleDriveMimeType.FOLDER
					.getMimeType());

			service.files().insert(folder).execute();

			Log.i(MyApp.TAG, "Folder Created!");
		} catch (IOException e) {
			Log.e(MyApp.TAG, "Error=>" + e.toString());
		}

	}

	/**
	 * Verifies whether a folder with a certain name exists or not and hasn't
	 * been trashed
	 *
	 * @param name
	 * @return
	 */
	public static String folderExists(Drive service, String name) {
		String fileId = null;

		Log.i(MyApp.TAG, "DriveUtils.folderExists()");

		if (name != null) {
			try {
				Drive.Files.List list = service.files().list();
				list = list
						.setQ("mimeType='application/vnd.google-apps.folder' and trashed=false and title='"
								+ name + "'");
				FileList fileList = list.execute();
				List<File> items = fileList.getItems();
				Iterator<File> iterator = items.iterator();
				while (iterator.hasNext()) {
					File current = iterator.next();
					Log.i(MyApp.TAG, "TITLE=>" + current.getTitle()
							+ " MimeType=" + current.getMimeType() + " Kind="
							+ current.getKind() + " ETAG=" + current.getEtag()
							+ " ID=" + current.getId());
					fileId = current.getId();
				}
			} catch (Exception ioe) {
				Log.e(MyApp.TAG, ioe.toString());
			}
		}

		return fileId;
	}

	/**
	 * Retrieve a list of Change resources.
	 *
	 * @param service
	 *            Drive API service instance.
	 * @param startChangeId
	 *            , ID of the change to start retrieving subsequent changes from
	 *            or {@code null}.
	 * @return List of Change resources.
	 */
	public static DriveUtils.ChangesResult retrieveAllChanges(Drive service,
			long startChangeId) {

		Log.i(MyApp.TAG, "DriveUtils.retrieveAllChanges()");

		DriveUtils.ChangesResult changesResult = new DriveUtils.ChangesResult();
		changesResult.result = new ArrayList<Change>();
		changesResult.lastResultId = startChangeId;

		Changes.List request;
		try {
			request = service.changes().list();

			if (startChangeId > 0) {
				request.setStartChangeId(startChangeId);
			}
			do {
				try {
					ChangeList changes = request.execute();

					if (changes.getLargestChangeId() != startChangeId) {
						changesResult.result.addAll(changes.getItems());
					}
					request.setPageToken(changes.getNextPageToken());
					changesResult.lastResultId = changes.getLargestChangeId();
				} catch (IOException e) {
					System.out.println("An error occurred: " + e);
					request.setPageToken(null);
				}
			} while (request.getPageToken() != null
					&& request.getPageToken().length() > 0);

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return changesResult;
	}

	/**
	 * Changes in folder specified by its id whose changes are after the time
	 * specified
	 *
	 * @return
	 */
	public static List<File> retrieveChangesFromFolder(Drive service,
			String folderId, String initialDateTime) {

		Log.i(MyApp.TAG, "DriveUtils.retrieveChangesFromFolder()");

		List<File> changeList = new ArrayList<File>();

		try {
			Drive.Files.List list = service.files().list();

			String q = "'" + folderId + "' in parents";
			if (initialDateTime != null) {
				// 2013-02-13T02:40:00-03:00
				q += " and modifiedDate>='" + initialDateTime + "'";
			}
			list = list.setQ(q);
			FileList fileList = list.execute();
			List<File> items = fileList.getItems();
			changeList.addAll(items);
		} catch (Exception ioe) {
			Log.e(MyApp.TAG, ioe.toString());
		}

		return changeList;
	}

	public static void printFileList(List<File> items) {

		Log.i(MyApp.TAG, "DriveUtils.printFileList()");

		Iterator<File> iterator = items.iterator();
		while (iterator.hasNext()) {
			File current = iterator.next();
			Log.i(MyApp.TAG, "TITLE=>" + current.getTitle() + " MimeType="
					+ current.getMimeType() + " Kind=" + current.getKind()
					+ " ETAG=" + current.getEtag() + " ID=" + current.getId());
		}
	}

	/**
	 * Represents the list with all the existing changes in Drive since the last
	 * specified result ID
	 *
	 * @author XGPB43
	 */
	public static class ChangesResult {
		public List<Change> result;
		public Long lastResultId;
	}

	public static void printChangesResult(ChangesResult changesResult) {
		List<Change> changeList = changesResult.result;
		Log.i(MyApp.TAG, "Drive.printChangesResult");
		Log.i(MyApp.TAG, "CHANGES NUMBER # => " + changeList.size());
		Iterator<Change> changeIterator = changeList.iterator();

		int i = 0;
		while (changeIterator.hasNext()) {
			Change currentChange = changeIterator.next();
			// Log.i(MyApp.TAG, "Change=>"+currentChange.getFileId());
			File file = currentChange.getFile();
			if (file != null) {
				Log.i(MyApp.TAG,
						"File #" + i + " EXISTS KIND="
								+ currentChange.getKind() + " TITLE="
								+ file.getTitle() + " "
								+ file.getModifiedDate());
			} else {
				Log.i(MyApp.TAG, "File #" + i + " DELETED! KIND="
						+ currentChange.getKind());
			}
			i++;
		}

	}

	/**
	 * Download a file's content.
	 *
	 * @param service
	 *            Drive API service instance.
	 * @param file
	 *            Drive File instance.
	 * @return InputStream containing the file's content if successful,
	 *         {@code null} otherwise.
	 */
	public static InputStream downloadFile(Drive service, File file) {

		String downloadUrl = file.getWebContentLink();

		Log.i(MyApp.TAG, "DriveUtils.downloadFile()");
		Log.i(MyApp.TAG, "DOWNLOAD URL=>" + downloadUrl);

		downloadUrl = file.getExportLinks().get("application/pdf");

		Log.i(MyApp.TAG, "DOWNLOAD URL=>" + downloadUrl);

		if (downloadUrl != null && downloadUrl.length() > 0) {
			try {
				HttpResponse resp = service.getRequestFactory()
						.buildGetRequest(new GenericUrl(downloadUrl)).execute();
				return resp.getContent();
			} catch (IOException e) {
				// An error occurred.
				e.printStackTrace();
				return null;
			}
		} else {
			// The file doesn't have any content stored on Drive.
			return null;
		}
	}

	/**
	 * Download all the files in changes list to be saved into the specified
	 * output folder
	 *
	 * @param service
	 * @param changes
	 * @param outputFolderPath
	 */
	public static void downloadFiles(Drive service, List<File> changes,
			String outputFolderPath) {

		Log.i(MyApp.TAG, "DriveUtils.downloadFiles()");

		File currentFile = null;
		Iterator<File> changesIterator = changes.iterator();

		while (changesIterator.hasNext()) {
			currentFile = changesIterator.next();

			// String downloadUrl = currentFile.getDownloadUrl();
			// Log.i(MyApp.TAG, "Download URL=>" + downloadUrl);
			// if (downloadUrl == null) {
			// downloadUrl = currentFile.getAlternateLink();
			// Log.i(MyApp.TAG, "AlternateLink URL=>" + downloadUrl);
			// }

			InputStream is = downloadFile(service, currentFile);
			if (is != null) {
				// Read all the content and save it to stoage
				String filePath = outputFolderPath + java.io.File.separator
						+ currentFile.getTitle() + ".pdf";
				Log.i(MyApp.TAG, "File PAth=>" + filePath);
				FileOutputStream fos = null;
				try {
					java.io.File outFile = new java.io.File(filePath);
					if (outFile.exists() == false) {
						outFile.createNewFile();
					}
					fos = new FileOutputStream(filePath);
					byte[] buffer = new byte[BUFFER_SIZE];
					int readBytes = 0;
					while ((readBytes = is.read(buffer, 0, BUFFER_SIZE)) != -1) {
						fos.write(buffer, 0, readBytes);
					}
					fos.close();
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Log.i(MyApp.TAG, "Nothing to download");
			}

		}
	}

	/**
	 * Insert new file.
	 *
	 * @param service
	 *            Drive API service instance.
	 * @param title
	 *            Title of the file to insert, including the extension.
	 * @param description
	 *            Description of the file to insert.
	 * @param parentId
	 *            Optional parent folder's ID.
	 * @param mimeType
	 *            MIME type of the file to insert.
	 * @param filename
	 *            Filename of the file to insert.
	 * @return Inserted file metadata if successful, {@code null} otherwise.
	 */
	public static File insertFile(Drive service, java.io.File contentFile,
			String parentId) {

		// File's metadata.
		File body = new File();
		body.setTitle(contentFile.getName());
		body.setDescription(contentFile.getName());

		// Get Mime-Type
		String mimeType = fileNameMap.getContentTypeFor(contentFile.getName());
		Log.i(MyApp.TAG, "MIME TYPE=>" + mimeType);

		body.setMimeType(mimeType);

		Log.i(MyApp.TAG, "DriveUtils.insertFile()");

		// Set the parent folder.
		if (parentId != null && parentId.length() > 0) {
			body.setParents(Arrays.asList(new ParentReference().setId(parentId)));
		}

		// File's content.
		FileContent mediaContent = new FileContent(mimeType, contentFile);
		try {
			File file = service.files().insert(body, mediaContent).execute();
			// Uncomment the following line to print the File ID.
			Log.i(MyApp.TAG, "File ID: %s" + file.getId());
			return file;
		} catch (IOException e) {
			Log.e(MyApp.TAG, "An error occured: " + e);
			return null;
		}
	}

	public static long getInitialTimeInMillis() {

		Log.i(MyApp.TAG, "DriveUtils.getInitialTimeInMillis()");

		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT-0300"));
		cal.set(1978, 24, 11, 10, 30, 0);
		return cal.getTime().getTime();
	}

	//public static
}
