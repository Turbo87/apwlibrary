Android PDF Writer
http://coderesearchlabs.com/androidpdfwriter


DESCRIPTION
-----------
A simple library to write simple PDF documents in Android.

USAGE
-----
Look at the PDFWriterDemo.java for a demonstration on how to use it, is very straightfoward.

NOTES
-----
I wrote this library because I couldn't find a library to write PDFs in Android and I needed one to complete a weekend project.
So, the current APW library was coded in a weekend (May 14-16, 2010) following the PDF 1.4 Reference (I think my implementation is compatible with lower PDF versions also), so expect limited functionality: it handles only 1 Page and provides limited functionality to it (writes text and paint lines... well, it covers pretty well the needs that my weekend project had at least, and may be it also address the needs of many other android projects out there with simple reporting needs).
There are so many things that can be added (images, filters, etc) and enhanced (it does not handle key/values for instance, etc etc etc), so if you add something that is useful for you (I think this library is a good base to be extended, check the class structure), please let me know since it might be helpful for others too.
UPDATE: Six months after that (Nov 15, 2010) I'm adding an overload to setPageFont() with an encoding parameter due to a user requirement for writing special characters in text. Check the PDF 1.4 Reference APPENDIX D, to be sure which encoding fits better for your needs, since as you can see there are symbols that have no octal value in all of the encodings.

LICENSE
-------
BSD. Check the LICENSE.txt file for more details.

TOOLS
-----
Check TOOLS.html to see a list of the tools involved in the development of this project.

HISTORY
-------
2010.05.19 - initial release
2010.11.15 - added setPageFont() overload with encoding parameter


Enjoy,
Javier Santo Domingo
http://coderesearchlabs.com
