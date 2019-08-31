## Purpose
- This software is designed to be a scientific event manager<br>

## History
- This software was developed mostly in 2015 using Java and mySQL by me (lucianourgal) alone.<br>
- It has beeen used in IFPR SEPIN'S (main scientific and cultural IFPR event) since 2015.<br>
- Its use has saved hundreads work hours and thousands of dollars.<br>

## Main features
- User inputs validations<br>
- Authors registration and updates<br>
- Presentation registration and updates<br>
- Sends a e-mail every time a presentation is inserted, updated or deleted<br>
- Presentation evaluators registration and updates<br>
- Manager by 'unidade'/campus definition by central comission<br>
- Evaluators per presentation assignment (configurable to allow from different areas or not)<br>
- Presentation's evaluation grades easy insertion and updates by evaluators<br>
- Automated Ranking of best presentations by area calculation<br>
- Automated evaluation forms generation<br>
- Automated complete event report generation in .xls spreadsheet<br>
- Automated Event Proceedings HTML generation<br>
- Automated Event certificates PDF generation using JRXML (Jasper reports)<br>
- Customizable system behavior throught inteface<br>
- Presentations and evaluators registry allowance throught ticket codes owned by central commission<br>
- Allows central comission to mark evaluators as non present to redistribute presentations evaluations and remove certificate from missing evaluators<br>

## About the Code
- Connection to mySQL using SSH tunneling<br>
- SQL injection resistant<br>
- Connection retry for unstable networks<br>
- MVC like structure<br>
- Java 1.7 compatible. Java Swing interface<br>
- Developed using NetBeans IDE<br>
- Variables, comments, interface and generated .xls and .pdf are in portuguese language (PT-br)<br>
- All java libraries included in 'LibrarysSEPIN v16.05.21.zip'<br>

## Personal data do configurate
- Database access at src/ClassesAuxiliares/BancoDeDados.java<br>
- Email address and password at src/ClassesAuxiliares/mailSender.java<br>
- Certificates and Evaluation background images at lib/<br>
- Your CPF to create a user at the end of 'MySQL initial data.txt'<br>
- Your event name and 'unidade'/campi at 'MySQL initial data.txt'<br>