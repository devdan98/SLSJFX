import java.util.ArrayList;

/** Collection class to hold a list of Contest objects (each stop of the 2024 SLS tour)
 * @author Daniel Reid (u2143528)
 * @version 06/03/2024
 */
public class ContestList {
    // Attributes
    private final ArrayList<Contest> contestList;

    /** Constructor initiates the ArrayList
     */
    public ContestList() {
        contestList = new ArrayList<>();
    }

    public final int MAX = 8; // There are only 8 stops on an SLS tour

    /** Checks if the contest list is full
     * @return Returns true if the list has reached 8 (maximum capacity)
     */
    public boolean isFull() {
        return contestList.size() == MAX;
    }

    /** Checks if the contest list is empty
     * @return Returns true if the list has 0 stops
     */
    public boolean isEmpty() {
        return contestList.isEmpty();
    }

    /** Gets the total number of stops
     * @return Returns the total number of stops currently in the list
     */
    public int getTotal() {
        return contestList.size();
    }

    /** Adds a new contest to the end of the list
     * @param cIn: the contest we are adding
     * @return Returns true or false depending on whether the object was added successfully or not
     */
    public boolean addContest(Contest cIn) {
        if (!isFull()) {
            contestList.add(cIn);
            return true;
        } else {
            System.out.println("List full, cannot add: " + cIn);
            return false;
        }
    }

    /** Removes a contest from the list
     * @param index: the contest we are removing
     */
    public void removeContest(int index) {
        if (index >= 0 && index < getTotal()) {
            contestList.remove(index);
            System.out.println("Contest removed from list...");
        } else {
            System.out.println("Invalid index!");
        }
    }

    /** Reads the contest at the given position
     * @param positionIn: The position of the contest in the list
     * @return Returns the contest's position in the list or null if there is none
     */
    public Contest getContest(int positionIn) {
        // check for valid position
        if(positionIn < 1 || positionIn > getTotal()) {
            // no object found at given position
            return null;
        } else {
            // subtract 1 from position
            return contestList.get(positionIn - 1);
        }
    }

    /** Returns a contest's index position from it's name
     * @param name: the name (location) of the contest
     * @return Returns the contest position in the list or null if there is no contest with the specified name
     */
    public Contest getContestByName(String name) {
        for (Contest contest : this.contestList) {
            if (contest.getLocation().equals(name)) {
                return contest;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return contestList.toString();
    }

}
