/**
 * 
 */
package org.verapdf.report;

import java.io.File;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
@XmlRootElement(name = "item")
public class ItemDetails {
	static final ItemDetails DEFAULT = new ItemDetails();
	@XmlElement
	private final String name;
	@XmlAttribute
	private final long size;

	private ItemDetails() {
		this("unknown");
	}

	private ItemDetails(final String name) {
		this(name, -1);
	}

	private ItemDetails(final String name, final long size) {
		this.name = name;
		this.size = size;
	}

	/**
	 * @return the item name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the size of the item in bytes
	 */
	public long getSize() {
		return this.size;
	}

	/**
	 * @param file
	 *            the {@link File} to extract details from
	 * @return a new {@code ItemDetails} instance initialised from the passed
	 *         file.
	 */
	public static ItemDetails fromFile(final File file) {
		return fromValues(file.getAbsolutePath(), file.length());
	}

	/**
	 * @param name
	 *            a name to identify the item
	 * @return a new {@code ItemDetails} instance with the given name and -1
	 *         (unknown) as the size in bytes;
	 */
	public static ItemDetails fromValues(final String name) {
		return fromValues(name, -1);
	}

	/**
	 * @param name
	 *            a name to identify the item
	 * @param size
	 *            the size of the item in bytes.
	 * @return a new {@code ItemDetails} instance initialised from the passed
	 *         parameters
	 */
	public static ItemDetails fromValues(final String name, final long size) {
		if (name == null)
			throw new NullPointerException("Parameter name can not be null.");
		return new ItemDetails(name, size);
	}

	/**
	 * @return the default ItemDetails instance
	 */
	public static ItemDetails defaultInstance() {
		return DEFAULT;
	}
}
