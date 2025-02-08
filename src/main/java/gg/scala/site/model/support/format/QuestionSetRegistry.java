package gg.scala.site.model.support.format;

public class QuestionSetRegistry {

    public static final QuestionSet GENERAL_SUPPORT = new QuestionSet()
            .add("What is your enquiry?", 5)
            .add("Additional information (optional)", 3);

    public static final QuestionSet PUNISHMENT_QUESTIONS = new QuestionSet()
            .add("What is your punishment ID?", 1)
            .add("Where were you punished? (Discord,Minecraft,Forums)", 1)
            .add("What is your in-game name?", 1)
            .add("What were you punished for?", 1)
            .add("Do you agree this punishment was fair?", 1)
            .add("If answered no, why do you think this punishment was unfair?", 3)
            .add("Have you been punished before in the same category?", 1)
            .add("Why should we revoke your punishment?", 3)
            .add("What is the Punisment Appeal Code? *hint: Read the Appeal Guidelines", 1)
            .add("Please include a link to your last appeal if you have one.", 3)
            .add("Additional Information", 3);

    public static final QuestionSet STAFF_APPLICATION = new QuestionSet()
            .add("What is your preferred name?", 1)
            .add("What is your Discord Tag (@user) and Discord ID", 1)
            .add("How old are you?", 1)
            .add("What is your timezone and country?", 1)
            .add("How many hours can you be active per day?", 1)
            .add("How many hours have you contributed on our server? (You can get your playtime with /playtime)", 1)
            .add("How did you find out about Scala?", 1)
            .add("Do you have a functioning microphone, and do you have easy access to it?", 1)
            .add("What is your pastt experience in Staffing?", 3)
            .add("List a few mentionable servers you have worked on (If any).", 3)
            .add(" List link(s) showcasing proof of your past work experience.", 3)
            .add("Why do you want to work on Solar?", 3)
            .add("What can you contribute to Scala?", 3)
            .add("How active are you (presently)?", 3)
            .add("How active can you be, upon joining our team?", 1)
            .add("What do you like to do in your free time?", 3)
            .add("Would you call yourself a 'team player'?", 3)
            .add("Would you call yourself 'consistent'? (Yes/No)", 1)
            .add("Would you call yourself 'responsible'? (Yes/No)", 1)
            .add("Do you understand that your application can take up to 7 days before being reviewed?", 1)
            .add("Do you understand that asking a staff, or conversating about your application in any Scala-based environment will result in an instant deny?", 1)
            .add("Do you understand that any propaganda found on your application, will mean you're unable to apply for 8 months?", 1)
            .add("Do you understand that your application can be denied without a given reason?", 1);

    public static final QuestionSet TRANSLATOR_APPLICATION = new QuestionSet()
            .add("What languages can you speak?", 3)
            .add("How fluently can you speak those languages?", 3)
            .add("How many hours a day can you be available to translate?", 3)
            .add("What is your discord and telegram @?", 2);

    public static final QuestionSet BUILDER_APPLICATION = new QuestionSet()
            .add("What is your in-game name?", 1)
            .add("What is your Discord Tag and Teleagram @?", 2)
            .add("How old are you?", 1)
            .add("What is your timezone and country?", 1)
            .add("Are you active on Discord?", 1)
            .add("For how long have you been playing Scala?", 1)
            .add("Have you ever been punished on Scala?", 1)
            .add("Why do you want to become a Builder?", 3)
            .add("Are you currently a Builder on a different server?", 1)
            .add("Have you ever been a builder on any other servers? If so, what kind of experience do you have?", 3)
            .add("Builder Portfolio: Share the link to the images of your creations, or the website in which you have all of your creations listed in.", 3)
            .add("How much hours can you dedicate to your builder duties weekly?", 1)
            .add("Do you have any experience with WorldEdit?", 1)
            .add("Do you have any experience with VoxelSniper?", 1)
            .add("Do you have any experience with GoPaint?", 1)
            .add("Do you have any experience with GoBrush?", 1);


    public static final QuestionSet BUG_REPORT = new QuestionSet()
            .add("Minecraft Version", 1)
            .add("Detailed Description of the Bug", 5)
            .add("Expected Outcome", 3)
            .add("Actual Outcome", 3)
            .add("Steps to Reproduce", 3)
            .add("What proxy did this occur on?", 1)
            .add("Additional Information (optional)", 3);
//            .add("Provide details about this bug", 5)
//            .add("How can you replicate this bug?", 3)
//            .add("Additional information (optional)", 3);

    public static final QuestionSet USER_REPORT = new QuestionSet()
            .add("In game name of the {type}", 1)
            .add("Provide details about the situation", 5)
            .add("Additional information (optional)", 3);

    public static final QuestionSet PAYMENT_SUPPORT = new QuestionSet()
            .add("What is your transaction ID?", 1)
            .add("Provide details about the situation", 5)
            .add("Additional information (optional)", 3);

    public static final QuestionSet MEDIA_APPLICATION = new QuestionSet()
            .add("Are you applying for Media or Famous (Include if for YouTube or Twitch or Kick,etc)?", 1)
            .add("What is your Minecraft IGN?", 1)
            .add("What is your discord and telegram @ (Telegram isnt required but is helpful)?", 2)
            .add("How old are you?", 1)
            .add("What languages can you speak?", 1)
            .add("What is your timezone and country?", 1)
            .add("For how long have you been playing Scala?", 1)
            .add("What could you do to help improve and expand the Network? *Be as descriptive as possible.*", 3)
            .add("Have you ever been punished on Scala? If so, why?", 3)
            .add("How many subscribers / followers do you have?", 1)
            .add("Do you display Scala's IP on your channel / videos?", 1)
            .add("How many videos can you upload per month?", 1)
            .add("How many times can you stream per month?", 1)
            .add("Link two or more videos on Scala", 2)
            .add("Do you agree to follow the Scala rules?", 1)
            .add("Provide a link to your platform", 1);
}