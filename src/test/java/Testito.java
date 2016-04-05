import org.junit.Test;

public class Testito {

    @Test
    public void test() {
        System.out.println("asdasdassd");

        final String s = "&lt;#list items as item&gt;".replaceAll("&lt;#list (.*)&gt;", "<#list $1>");
        System.out.println(s);

    }

            /*
            "<h1>Hello ${name}!</h1>\n\n<p>We Wanted to reach you out, about ${dog.name},
             we know you love your ${dog.age} years old ${dog.breed}</p>\n\n<p>
             Here are some services that might
             interest you of him!</p>\n
             \n<ul>\n\t<li>&lt;#list items as item&gt;\n\t<ul>\n\t\t<li>
             ${item.name}, ${item.price}</li>\n\t</ul>\n\t<!--#list--></li>\n</ul>\n"
             */

}
