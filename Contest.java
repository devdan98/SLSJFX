/** Class used to store the details of a single contest in the 2024 SLS Championship Tour
 * @author Daniel Reid (u2143528)
 * @version 06/03/2024
 */
public class Contest {
    private final String location;
    private final String date;

    /** Constructor initialises the location and date of the contest
     * @param locationIn: location of the contest
     * @param dateIn: date of the contest
     */
    public Contest(String locationIn, String dateIn) {
        location = locationIn;
        date = dateIn;
    }

    /** Reads the location
     * @return Returns the location of the current contest
     */
    public String getLocation() {
        return location;
    }

    /** Reads the date
     * @return Returns the date of the current contest
     */
    public String getDate() {
        return date;
    }

    /** Overriding to create a neater way of printing a Contest
     * @return Returns the formatted string with the location and date
     */
    @Override
    public String toString() {
        return "(" + location.toUpperCase() + ", " + date + ")";
    }
}
