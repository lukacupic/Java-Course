package hr.fer.zemris.hw18.model;

/**
 * Represents a picture, encapsulating the name of the picture,
 * description and the tags.
 *
 * @author Luka Čupić
 */
public class Picture {

    /**
     * Name of the picture.
     */
    private String name;

    /**
     * Description for the picture.
     */
    private String description;

    /**
     * Tags for the picture.
     */
    private String[] tags;

    /**
     * Creates a new picture.
     *
     * @param name        picture's name
     * @param description picture's description
     * @param tags        picture's tags
     */
    public Picture(String name, String description, String[] tags) {
        this.name = name;
        this.description = description;
        this.tags = tags;
    }

    /**
     * Gets the name of the picture.
     *
     * @return the name of the picture
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the picture.
     *
     * @param name the name of the picture
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the picture.
     *
     * @return the description of the picture
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the picture.
     *
     * @param description the description of the picture
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the tags of the picture.
     *
     * @return the tags of the picture
     */
    public String[] getTags() {
        return tags;
    }

    /**
     * Sets the tags of the picture.
     *
     * @param tags the tags of the picture
     */
    public void setTags(String[] tags) {
        this.tags = tags;
    }
}