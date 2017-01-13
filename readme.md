# Description
This is a project to play around with elasticsearch plugins. I'am going to demonstrate the options you have when creating your own plugins.

# Step one, create a plugin that does nothing
This is actually copy pasting code from the blogpost from David Pilato
http://david.pilato.fr/blog/2016/10/16/creating-a-plugin-for-elasticsearch-5-dot-0-using-maven-updated-for-ga/

Biggest part was configuring maven. The other part was configuring the plugin class *BasicPlugin* and the test to check if the plugin was installed.

# Step two, create my own rest end point
This is again mostly a copy paste from another blogpost from David Pilato
http://david.pilato.fr/blog/2016/10/19/adding-a-new-rest-endpoint-to-elasticsearch-updated-for-ga/

Figuring out you have to extend the *Plugin* class and implement interfaces like *ActionPlugin* and implement a class that extends the *BaseRestHandler* like the class *JettroRestAction*.

# Step three, create a custom TokenFilter 
Finally I wrote an analysis plugin with a new *TokenFilter* that I have called the *JettroOnlyTokenFilter*. Only special tokens pass this filter. You guess which one.

# What's next?
The coming weeks I'll clean up the code, document the plugin and start writing unit tests. Also going to implement a bit more serious extensions.
