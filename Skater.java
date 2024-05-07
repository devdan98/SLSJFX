/** Class used to record the details of a skateboarder participating in the SLS competitions
 * @author Daniel Reid (u2143528)
 * @version 06/03/2024
 */
public class Skater {
    private static final int NUM_SCORES = 7;
    private static final double MAX_SCORE = 100.00;
    private final String name;
    private final String stance;
    private final String nationality;
    private final String gender;
    private final SkaterScore skaterScore;

    /** Constructor initialises the name, stance, nationality, and gender of the Skater
     * @param nameIn: name of skater
     * @param stanceIn: stance of skater (can only be regular or goofy)
     * @param nationalityIn: nationality of skater
     * @param genderIn: gender of skater (for simplicity's sake can only be male or female)
     */
    public Skater(String nameIn, String stanceIn, String nationalityIn, String genderIn) {
        name = nameIn;

        // Stance validation
        if (!stanceIn.equalsIgnoreCase("regular") && !stanceIn.equalsIgnoreCase("goofy")) {
            throw new IllegalArgumentException("Stance must be either 'Regular' or 'Goofy'");
        }
        stance = stanceIn;

        nationality = nationalityIn;

        // Gender validation
        if (!genderIn.equalsIgnoreCase("male") && !genderIn.equalsIgnoreCase("female")) {
            throw new IllegalArgumentException("Gender must be either 'Male' or 'Female'");
        }
        gender = genderIn;

        skaterScore = new SkaterScore();
    }

    /** Reads the name of the Skater
     * @return Returns the name of the skater
     */
    public String getName() {
        return name;
    }

    /** Reads the stance of the Skater
     * @return Returns the stance of the skater
     */
    public String getStance() {
        return stance;
    }

    /** Reads the nationality of the Skater
     * @return Returns the nationality of the skater
     */
    public String getNationality() {
        return nationality;
    }

    /** Reads the gender of the Skater
     * @return Returns the gender of the skater
     */
    public String getGender() {
        return gender;
    }

    /** Add scores for the skater at a specific contest
     * @param contest: the contest we wish to add scores to
     * @param scoresArray: the array of scores we wish to add
     */
    public void addScores(Contest contest, Double[] scoresArray) {
        // Check if the array length is exactly 7
        if (scoresArray.length != NUM_SCORES) {
            throw new IllegalArgumentException("Exactly 7 scores (2 runs and 5 tricks) must be provided.");
        }

        // Check if all scores are within the valid range
        for (Double score : scoresArray) {
            if (score < 0.0 || score > MAX_SCORE) {
                throw new IllegalArgumentException("Scores must be between 0.0 and 100.0.");
            }
        }

        // Add scores if all checks pass
        skaterScore.addScores(contest, scoresArray);
    }


    /** Get scores for the skater at a specific contest
     * @return the scores for the skater
     */
    public Double[] getScores(Contest contest) {
        return skaterScore.getScores(contest);
    }

    public double getTotalScore(Contest contest) {
        double total = 0;

        Double[] scores = skaterScore.getScores(contest);

        if (scores == null) {
            return total;
        }

        for (Double score : scores) {
            total += score;
        }

        return total;
    }


    /** Get true or false if the skater has scores in a specific contest
     * @return true or false if the skater has scores
     */
    public boolean hasScores() {
        return skaterScore.hasScores();
    }

    @Override
    public String toString() {
        return "(" + name + ", " + stance + ", " + nationality + ", " + gender + ")";
    }
}