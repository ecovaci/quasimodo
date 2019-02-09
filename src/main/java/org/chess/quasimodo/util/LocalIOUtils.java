/*******************************************************************************
 * Quasimodo - a chess interface for playing and analyzing chess games.
 * Copyright (C) 2011 Eugen Covaci.
 * All rights reserved.
 *  
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 ******************************************************************************/
package org.chess.quasimodo.util;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.chess.quasimodo.errors.EngineException;
import org.chess.quasimodo.errors.PlayerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;


public class LocalIOUtils {
	private static final Logger logger = LoggerFactory.getLogger(LocalIOUtils.class);
	
    public static void storeProperties (Properties properties, String comments, File toFile)
    throws IOException {
    	FileOutputStream outputStream = null;
 		try {
 			 outputStream = new FileOutputStream(toFile);
 			 properties.store(outputStream, comments);
 		} catch (FileNotFoundException e) {
 			throw new IOException("File not found");
 		} catch (IOException e) {
 			throw e;
 		} finally {
 			if (outputStream != null) {
 				try {
 					outputStream.close();
 				} catch (IOException e) {}
 			}
 		}
    }
    
    public static void storeProperties (Properties properties, File toFile)
    throws IOException {;
    	storeProperties(properties, null, toFile);
    }
    
    public static void storeProperties (Properties properties, String comments, String filepath)
    throws IOException {
    	storeProperties(properties, comments, new File(filepath));
    }
    
    public static void loadProperties (final Properties properties,File fromFile) 
    throws IOException {
    	FileInputStream inputStream = null;
 		try {
 			inputStream = new FileInputStream(fromFile);
 			properties.load(inputStream);
 		} catch (FileNotFoundException e) {
 			throw new IOException("File not found");
 		} catch (IOException e) {
 			throw e;
 		} finally {
 			if (inputStream != null) {
 				try {
 					inputStream.close();
 				} catch (IOException e) {}
 			}
 		}
    }
    
    /**
	  * Get the String residing on the clipboard.
	  * @return Any text found on the Clipboard; if none found, return a null String.
     * @throws IOException 
	  */
	public static String getClipboardContents() throws IOException {
	    String result = null;
	    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    Transferable contents = clipboard.getContents(null);
	    if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
		    try {
		        result = (String)contents.getTransferData(DataFlavor.stringFlavor);
		    } catch (UnsupportedFlavorException ex) {
		    	throw  new IOException(ex);
		    } catch (IOException ex) {
		        throw ex;
		    }
	    }
	    return result;
	}
	
    public static File getAppHomeDirectory () {
    	return new File(SystemUtils.getUserHome().getAbsolutePath() 
    			+ File.separator + ".quasimodo" );
    }
/*    
    public static File getEngineDescrFile ()  {
    	return new File(getAppHomeDirectory().getAbsolutePath() 
    			+ File.separator + "engines.properties");
    }
    
    public static File getDesignFile () {
    	return new File(getAppHomeDirectory().getAbsolutePath() 
    			+ File.separator + "design.properties");
    }
    
    public static File getConfigFile () {
    	return new File(getAppHomeDirectory().getAbsolutePath() 
    			+ File.separator + "config.properties");
    }
    
    public static File getECOFile () {
    	return new File(getAppHomeDirectory().getAbsolutePath() + File.separator 
    			+ "database" + File.separator + Constants.ECO_DATABASE_FILENAME);
    }*/
    
    public static int getNextEngineId (File enginesDir) throws EngineException {
    	try {
			return getNextId (enginesDir);
		} catch (Exception e) {
			throw new EngineException(e);
		}
    }
    
    public static int getNextPlayerId (File playersDir) throws PlayerException {
    	try {
			return getNextId (playersDir);
		} catch (Exception e) {
			throw new PlayerException(e);
		}
    }
    
    public static int getNextId (File location) {
    	try {
			int maxId = 0;
			if (location.exists()) {
				for (String filename:location.list()) {
					if (filename.matches("\\d+")) {
						maxId = Math.max(maxId, Integer.parseInt(filename));
					}
				}
			} 
			return maxId + 1;
		} catch (Exception e) {
			throw new IllegalStateException("Failed to get next id", e);
		}
    }
    
	public static void unzip(File arhivePath, File destinationDirectory) throws IOException {
		ZipInputStream inputZip = null;
		try {
			inputZip = new ZipInputStream(new BufferedInputStream(new FileInputStream(arhivePath)));
			if (!destinationDirectory.exists()) {
				destinationDirectory.mkdirs();
			}
			int buffer_size = 2048;
			BufferedOutputStream destination = null;
			ZipEntry entry;
			int count;
			while ((entry = inputZip.getNextEntry()) != null) {
				logger.debug("Extracting: " + entry);
				byte data[] = new byte[buffer_size];
				// write the files to the disk
				if (entry.isDirectory()) {
					new File(destinationDirectory + File.separator + entry.getName()).mkdirs();
				} else {
					try {
						destination = new BufferedOutputStream(
								new FileOutputStream(destinationDirectory + File.separator + entry.getName()), buffer_size);
						while ((count = inputZip.read(data, 0, buffer_size)) != -1) {
							destination.write(data, 0, count);
						}
					} catch (IOException e) {
						throw e;
					} finally {
						if (destination != null) {
						    destination.close();
						}
					}
				}
			}
		} catch (Exception e) {
			throw new IOException("Error on extracting arhive: " + arhivePath, e);
		} finally {
			if (inputZip != null) {
				try {
					inputZip.close();
				} catch (Exception e) {}
			}
		}
	}   
	
	public static File getPieceSet (String pieceSetFileName) throws IOException {
		File arhiveDir = new File(Thread.currentThread().getContextClassLoader()
			      .getResource("images/pieces").getFile());
		File[] zipFiles = arhiveDir.listFiles(new ZipFilter());
		for (File arhive : zipFiles) {
            if (arhive.getName().equals(pieceSetFileName)) {
            	return arhive;
            }
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static String getPieceSetName (String pieceSetFileName) throws IOException {
		File arhiveDir = new File(Thread.currentThread().getContextClassLoader()
			      .getResource("images/pieces").getFile());
		File[] zipFiles = arhiveDir.listFiles(new ZipFilter());
		ZipFile inputZip = null;
		for (File arhive : zipFiles) {
			if (arhive.getName().equalsIgnoreCase(pieceSetFileName)) {
				try {
					inputZip = new ZipFile(arhive);
					Enumeration<ZipEntry> e = (Enumeration<ZipEntry>)inputZip.entries();
					ZipEntry entry;
					while (e.hasMoreElements()) {
						entry = e.nextElement();
						logger.debug("Extracting: " + entry);
						if (!entry.isDirectory() && "definition".equalsIgnoreCase(entry.getName())) {
							return PropertiesLoaderUtils.loadProperties(new InputStreamResource(inputZip.getInputStream(entry))).getProperty("name");
						} 
					}
					throw new IllegalArgumentException("");
				} catch (Exception e) {
					throw new IOException("", e);
				} finally {
					if (inputZip != null) {
						try {
							inputZip.close();
						} catch (Exception e) {}
					}
				}
			}
		}
		return null;
	}
	
	public static byte[] compress (byte[] input) {
		Deflater deflater = new Deflater();
	    deflater.setLevel(Deflater.BEST_COMPRESSION);
	    deflater.setInput(input);
	    deflater.finish();
	    ByteArrayOutputStream byteArray = new ByteArrayOutputStream(input.length);
	    byte[] buffer = new byte[1024];
	    while (!deflater.finished()) {
	      int compByte = deflater.deflate(buffer);
	      byteArray.write(buffer, 0, compByte);
	    }
	  
        try {
			byteArray.close();
		} catch (IOException e) {
			
		}
        return byteArray.toByteArray();
	}
	
	public static class ZipFilter implements FileFilter {

		@Override
		public boolean accept(File pathname) {
			if ("zip".equalsIgnoreCase(StringUtils.getFilenameExtension(pathname.getName()))) {
				return true;
			}
			return false;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Image> loadPiecesetImages (String piecesetFilename,String piecesetSize) throws IOException {
		Assert.isTrue(StringUtils.hasLength(piecesetFilename), "Empty piecesetFilename is not allowed");
		Assert.isTrue(StringUtils.hasLength(piecesetSize), "Empty piecesetSize is not allowed");
		Map<String, Image> pieceImageMap = new HashMap<String, Image>();
		File arhiveDir = new File(Thread.currentThread().getContextClassLoader()
			      .getResource("images/pieces").getFile());
		File[] zipFiles = arhiveDir.listFiles(new ZipFilter());
		ZipFile inputZip = null;
		for (File arhive : zipFiles) {
			if (arhive.getName().equalsIgnoreCase(piecesetFilename)) {
				try {
					inputZip = new ZipFile(arhive);
					Enumeration<ZipEntry> e = (Enumeration<ZipEntry>)inputZip.entries();
					ZipEntry entry;
					while (e.hasMoreElements()) {
						entry = e.nextElement();
						logger.debug("Extracting: " + entry);
						
						if (!entry.isDirectory() 
								&& entry.getName().startsWith(piecesetSize)
								&& "png".equalsIgnoreCase(StringUtils.getFilenameExtension(entry.getName()))) {
							pieceImageMap.put(FilenameUtils.getBaseName(entry.getName()), ImageIO.read(inputZip.getInputStream(entry)));
						} 
					}
				} catch (Exception e) {
					throw new IOException("Error loading pieceset images", e);
				} finally {
					if (inputZip != null) {
						try {
							inputZip.close();
						} catch (Exception e) {}
					}
				}
			}
		}
		return pieceImageMap;
	}
	
	public static void serializeToXML (Object object, File output) throws IOException {
		XMLEncoder encoder = null;
		try {
			encoder = new XMLEncoder(FileUtils.openOutputStream(output));
			encoder.writeObject(object);
		} finally {
			if (encoder != null) {
			    encoder.close();
			}
		}
	}
	
	public static void serializeToXML (Object object, String filepath) throws IOException {
		serializeToXML(object, new File(filepath));
	}
	
	public static <T> T deserializeFromXML (File input, Class<T> classType) throws IOException {
		XMLDecoder decoder = null;
		try {
			decoder = new XMLDecoder(FileUtils.openInputStream(input));
			return classType.cast(decoder.readObject());
		} finally {
			if (decoder != null) {
				decoder.close();
			}
		}
	}
	
	public static <T> T deserializeFromXML (String filepath, Class<T> classType) throws IOException {
		return deserializeFromXML(new File(filepath), classType);
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(getPieceSetName("merida.zip"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
