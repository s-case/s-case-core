package org.eclipse.wb.swt;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.osgi.framework.Bundle;

public class ResourceManager extends SWTResourceManager {
	private static Map<ImageDescriptor, Image> m_descriptorImageMap = new HashMap<ImageDescriptor, Image>();

	public static ImageDescriptor getImageDescriptor(Class<?> clazz, String path) {
		return ImageDescriptor.createFromFile(clazz, path);
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		try {
			return ImageDescriptor.createFromURL(new File(path).toURI().toURL());
		} catch (MalformedURLException localMalformedURLException) {
		}
		return null;
	}

	public static Image getImage(ImageDescriptor descriptor) {
		if (descriptor == null) {
			return null;
		}
		Image image = (Image) m_descriptorImageMap.get(descriptor);
		if (image == null) {
			image = descriptor.createImage();
			m_descriptorImageMap.put(descriptor, image);
		}
		return image;
	}

	@SuppressWarnings("unchecked")
	private static Map<Image, Map<Image, Image>>[] m_decoratedImageMap = new Map[5];

	public static Image decorateImage(Image baseImage, Image decorator) {
		return decorateImage(baseImage, decorator, 4);
	}

	public static Image decorateImage(Image baseImage, final Image decorator, final int corner) {
		if ((corner <= 0) || (corner >= 5)) {
			throw new IllegalArgumentException("Wrong decorate corner");
		}
		Map<Image, Map<Image, Image>> cornerDecoratedImageMap = m_decoratedImageMap[corner];
		if (cornerDecoratedImageMap == null) {
			cornerDecoratedImageMap = new HashMap<Image, Map<Image, Image>>();
			m_decoratedImageMap[corner] = cornerDecoratedImageMap;
		}
		Map<Image, Image> decoratedMap = (Map<Image, Image>) cornerDecoratedImageMap.get(baseImage);
		if (decoratedMap == null) {
			decoratedMap = new HashMap<Image, Image>();
			cornerDecoratedImageMap.put(baseImage, decoratedMap);
		}

		Image result = (Image) decoratedMap.get(decorator);

		if (result == null) {
			final Rectangle bib = baseImage.getBounds();
			final Rectangle dib = decorator.getBounds();

			final Point baseImageSize = new Point(bib.width, bib.height);
			CompositeImageDescriptor compositImageDesc = new CompositeImageDescriptor() {
				protected void drawCompositeImage(int width, int height) {
					drawImage(getImageData(), 0, 0);
					int bibwidth = bib.width;
					int bibheight = bib.height;
					int dibwidth = dib.width;
					int dibheight = dib.height;

					if (corner == 1) {
						drawImage(decorator.getImageData(), 0, 0);
					} else if (corner == 2) {
						drawImage(decorator.getImageData(), bibwidth - dibwidth, 0);
					} else if (corner == 3) {
						drawImage(decorator.getImageData(), 0, bibheight - dibheight);
					} else if (corner == 4) {
						drawImage(decorator.getImageData(), bibwidth - dibwidth, bibheight - dibheight);
					}
				}

				protected Point getSize() {
					return baseImageSize;
				}

			};
			result = compositImageDesc.createImage();
			decoratedMap.put(decorator, result);
		}
		return result;
	}

	public static void disposeImages() {

		for (Iterator<Image> I = m_descriptorImageMap.values().iterator(); I.hasNext();) {
			((Image) I.next()).dispose();
		}
		m_descriptorImageMap.clear();

		for (int i = 0; i < m_decoratedImageMap.length; i++) {
			Map<Image, Map<Image, Image>> cornerDecoratedImageMap = m_decoratedImageMap[i];
			if (cornerDecoratedImageMap != null) {
				for (Map<Image, Image> decoratedMap : cornerDecoratedImageMap.values()) {
					for (Image image : decoratedMap.values()) {
						image.dispose();
					}
					decoratedMap.clear();
				}
				cornerDecoratedImageMap.clear();
			}
		}

		for (Iterator<Image> I = m_URLImageMap.values().iterator(); I.hasNext();) {
			((Image) I.next()).dispose();
		}
		m_URLImageMap.clear();
	}

	private static Map<String, Image> m_URLImageMap = new HashMap<String, Image>();

	private static PluginResourceProvider m_designTimePluginResourceProvider = null;

	@Deprecated
	public static Image getPluginImage(Object plugin, String name) {
		try {
			URL url = getPluginImageURL(plugin, name);
			if (url != null) {
				return getPluginImageFromUrl(url);
			}
		} catch (Throwable localThrowable) {
		}

		return null;
	}

	public static Image getPluginImage(String symbolicName, String path) {
		try {
			URL url = getPluginImageURL(symbolicName, path);
			if (url != null) {
				return getPluginImageFromUrl(url);
			}
		} catch (Throwable localThrowable) {
		}

		return null;
	}

	private static Image getPluginImageFromUrl(URL url) {
		try {
			try {
				String key = url.toExternalForm();
				Image image = m_URLImageMap.get(key);
				if (image == null) {
					InputStream stream = url.openStream();
					try {
						image = getImage(stream);
						m_URLImageMap.put(key, image);
					} finally {
						stream.close();
					}
				}
				return image;
			} catch (Throwable e) {
				// Ignore any exceptions
			}
		} catch (Throwable e) {
			// Ignore any exceptions
		}
		return null;

	}

	@Deprecated
	public static ImageDescriptor getPluginImageDescriptor(Object plugin, String name) {
		try {
			URL url = getPluginImageURL(plugin, name);
			return ImageDescriptor.createFromURL(url);
		} catch (Throwable localThrowable1) {
		}

		return null;
	}

	public static ImageDescriptor getPluginImageDescriptor(String symbolicName, String path) {
		try {
			URL url = getPluginImageURL(symbolicName, path);
			if (url != null) {
				return ImageDescriptor.createFromURL(url);
			}
		} catch (Throwable localThrowable) {
		}

		return null;
	}

	private static URL getPluginImageURL(String symbolicName, String path) {
		Bundle bundle = Platform.getBundle(symbolicName);
		if (bundle != null) {
			return bundle.getEntry(path);
		}

		if (m_designTimePluginResourceProvider != null) {
			return m_designTimePluginResourceProvider.getEntry(symbolicName, path);
		}

		return null;
	}

	private static URL getPluginImageURL(Object plugin, String name) throws Exception {
		try {
			Class<?> BundleClass = Class.forName("org.osgi.framework.Bundle");
			Class<?> BundleContextClass = Class.forName("org.osgi.framework.BundleContext");
			if (BundleContextClass.isAssignableFrom(plugin.getClass())) {
				Method getBundleMethod = BundleContextClass.getMethod("getBundle", new Class[0]);
				Object bundle = getBundleMethod.invoke(plugin, new Object[0]);

				Class<?> PathClass = Class.forName("org.eclipse.core.runtime.Path");
				Constructor<?> pathConstructor = PathClass.getConstructor(new Class[] { String.class });
				Object path = pathConstructor.newInstance(new Object[] { name });

				Class<?> IPathClass = Class.forName("org.eclipse.core.runtime.IPath");
				Class<?> PlatformClass = Class.forName("org.eclipse.core.runtime.Platform");
				Method findMethod = PlatformClass.getMethod("find", new Class[] { BundleClass, IPathClass });
				return (URL) findMethod.invoke(null, new Object[] { bundle, path });
			}

		} catch (Throwable localThrowable) {
			Class<?> PluginClass = Class.forName("org.eclipse.core.runtime.Plugin");
			if (PluginClass.isAssignableFrom(plugin.getClass())) {
				Class<?> PathClass = Class.forName("org.eclipse.core.runtime.Path");
				Constructor<?> pathConstructor = PathClass.getConstructor(new Class[] { String.class });
				Object path = pathConstructor.newInstance(new Object[] { name });

				Class<?> IPathClass = Class.forName("org.eclipse.core.runtime.IPath");
				Method findMethod = PluginClass.getMethod("find", new Class[] { IPathClass });
				return (URL) findMethod.invoke(plugin, new Object[] { path });
			}
		}
		return null;
	}

	public static void dispose() {
		disposeColors();
		disposeFonts();
		disposeImages();
	}

	public static abstract interface PluginResourceProvider {
		public abstract URL getEntry(String paramString1, String paramString2);
	}
}
