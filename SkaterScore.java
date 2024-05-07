import java.util.HashMap;

/** Class used to store the scores for each skater at each contest
 * @author Daniel Reid (u2143528)
 * @version 06/03/2024
 */
public class SkaterScore {
    private final HashMap<Contest, Double[]> scores; // contest mapped to an array of scores

    /** Constructor initialises empty skater score hash map
     */
    public SkaterScore() {
        scores = new HashMap<>();
    }

    /** Add scores for a skater at a specific contest
     * @param contest: the contest we are adding scores to
     * @param scoresArray: the scores we are adding
     */
    public void addScores(Contest contest, Double[] scoresArray) {
        scores.put(contest, scoresArray);
    }


    /** Get scores for a skater at a specific contest
     * @param contest: the contest we wish to retrieve the scores from
     */
    public Double[] getScores(Contest contest) {
        return scores.get(contest);
    }

    /** Check to see if scores exist for the skater
     * @return true or false depending on if the list is populated or not
     */
    public boolean hasScores() {
        return !scores.isEmpty();
    }


}