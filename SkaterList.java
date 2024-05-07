import java.util.ArrayList;

/** Collection class to hold a list of skaters
 * @author Daniel Reid (u2143528)
 * @version 06/03/2024
 */
public class SkaterList {
    private final ArrayList<Skater> skaterList;
    public static final int MAX = 8;

    /** Constructor initialises the empty skater list
     */
    public SkaterList() {
        skaterList = new ArrayList<>();
    }

    /** Adds a new skater to the list
     * @param sIn: the skater to add
     * @return returns true if the skater was added correctly or false if not
     */
    public boolean addSkater(Skater sIn) {
        if(!isFull()) {
            skaterList.add(sIn);
            return true;
        } else {
            return false;
        }
    }

    /** Removes a skater from the list
     * @param index: the skater we are removing
     */
    public void removeSkater(int index) {
        if (index >= 0 && index < getTotal()) {
            skaterList.remove(index);
            System.out.println("Skater removed from list...");
        } else {
            System.out.println("Invalid index!");
        }
    }

    /** Reads the skater at the given position in the list
     * @param positionIn: the position of the skater in the list
     * @return Returns the skater at the given position in the list, or null if there is no skater
     */
    public Skater getSkater(int positionIn) {
        if (positionIn < 1 || positionIn > getTotal()) {
            return null;
        } else {
            return skaterList.get(positionIn - 1);
        }
    }

    /** Returns a skater's index position from it's name
     * @param name: the name of the skater
     * @return Returns the skater position in the list or null if there is no skater with the specified name
     */
    public Skater getSkaterByName(String name) {
        for (Skater skater : this.skaterList) {
            if (skater.getName().equalsIgnoreCase(name)) {
                return skater;
            }
        }
        return null;
    }

    /** Reports whether the list is empty or not
     * @return Returns true if the list is empty, false otherwise
     */
    public boolean isEmpty() {
        return skaterList.isEmpty();
    }

    /** Reports whether the list is full or not
     * @return Returns true if the list is full, false otherwise
     */
    public boolean isFull() {
        return skaterList.size() == MAX;
    }

    /** Gets the total number of skaters
     * @return Returns the total number of skaters currently in the list
     */
    public int getTotal() {
        return skaterList.size();
    }

    @Override
    public String toString() {
        return skaterList.toString();
    }
}