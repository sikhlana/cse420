package name.saifmahmud.cse420.Regex;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Processor
{
    private FileInputStream stream;

    public Processor(FileInputStream stream)
    {
        this.stream = stream;
    }

    public void process()
    {
        Scanner sc = new Scanner(stream);
        ArrayList<Pattern> patterns = new ArrayList<>();

        int patternCount = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < patternCount; i++)
        {
            patterns.add(new Pattern(sc.nextLine().trim()));
        }

        int textCount = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < textCount; i++)
        {
            int n = validateString(sc.nextLine().trim(), patterns);
            if (n > -1)
            {
                System.out.println("YES," + ++n);
            }
            else
            {
                System.out.println("NO,0");
            }
        }
    }

    private int validateString(String str, List<Pattern> patterns)
    {
        for (int i = 0; i < patterns.size(); i++)
        {
            Matcher matcher = new Matcher(patterns.get(i));

            if (matcher.match(str))
            {
                return i;
            }
        }

        return -1;
    }
}
