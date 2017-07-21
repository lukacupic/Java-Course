package hr.fer.zemris.java.hw16.trazilica;

import java.nio.file.Path;
import java.text.DecimalFormat;

/**
 * This class represents a document which contains words from
 * the main vocabulary. All characters that are not lowercase
 * or uppercase UTF-8 letters are ignored, as they are irrelevant
 * to the searching algorithm. The exact document's contents are
 * not stored in the memory, as they are represented by the TF-IDF
 * vector ({@see https://en.wikipedia.org/wiki/Tf%E2%80%93idf}).
 *
 * @author Luka Čupić
 */
public class Document {

	/**
	 * Represents the path to the document.
	 */
	private Path path;

	/**
	 * Represents the TF-IDF vector for the document.
	 */
	private Vector vector;

	/**
	 * Represents the TF vector component for the document. This
	 * vector is used as a helper-vector while creating the full
	 * {@link #vector} object.
	 */
	private Vector tfVector;

	/**
	 * Creates a new document object.
	 *
	 * @param path  the path to the represented document
	 * @param tf    the TF vector component
	 * @param tfidf the full TF-IDF vector representing the document
	 */
	public Document(Path path, Vector tf, Vector tfidf) {
		this.path = path != null ? path.toAbsolutePath() : null;
		this.tfVector = tf;
		this.vector = tfidf;
	}

	/**
	 * Returns the similarity coefficient between this and the specified
	 * document. In reality, the similarity coefficient is nothing other
	 * than the scalar product of this and the specified vector. The
	 * coefficient is a value on the interval [0, 1] where 0 means that
	 * the documents share no similarity whereas 1 means that the documents
	 * are identical.
	 *
	 * @param other the document to compare to
	 * @return the similarity between this and the provided document
	 */
	public double similarTo(Document other) {
		double sim = this.getVector().cos(other.getVector());
		DecimalFormat f = new DecimalFormat("#0.0000");
		return Double.parseDouble(f.format(sim).replace(",", "."));
	}

	/**
	 * Gets the path representing this document.
	 *
	 * @return the path to the document
	 */
	public Path getPath() {
		return path;
	}

	/**
	 * Sets the path for this document.
	 *
	 * @param path the path to the document
	 */
	public void setPath(Path path) {
		this.path = path.toAbsolutePath();
	}

	/**
	 * Gets the TF-IDF vector representing this document.
	 *
	 * @return the TF-IDF vector
	 */
	public Vector getVector() {
		return vector;
	}

	/**
	 * Sets the TF-IDF vector representing this document.
	 *
	 * @param vector the TF-IDF vector
	 */
	public void setVector(Vector vector) {
		this.vector = vector;
	}

	/**
	 * Gets the TF vector component of this document.
	 *
	 * @return the TF vector component
	 */
	public Vector getTFVector() {
		return tfVector;
	}

	/**
	 * Sets the TF vector component of this document.
	 *
	 * @param tfVector the TF vector component
	 */
	public void setTFVector(Vector tfVector) {
		this.tfVector = tfVector;
	}
}
