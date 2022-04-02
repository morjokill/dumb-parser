# **Dumb parser**

Parser gets content from the declared `URL`,
clears it from HTML tags and JS code then 
allows you to do dumb things with the raw 
text left after clearing

---

**API:** <br>
- `/page?url=` - raw page with all HTML and JS, not 
filtered. Practically `wget`
- `/content?url=` - page filtered from HTML and JS
- `/sort?url=` - sorts filtered page by characters 
in alphabetic order
- `/group?url=` - groups filtered page by characters 
and counts the amount of character's occurrences
- `/find?url=&word=` - counts amount of occurrences of the 
word and returns all the places word was found
in