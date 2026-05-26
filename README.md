# Parliament_Browser_4_Montag_3

PRG-Praktikum WiSe 2022 / 2023 Abschlussprojekt

## Parliament Browser (PRG-Praktikum, WS 22/23)
A full-stack client-server web application built during the Practical Programming Course at Goethe University Frankfurt to store, analyze, and visualize the stenographic protocols (XML) of the German Bundestag (19th legislative period onwards).

### Tech Stack
* **Backend:** Java 8, Maven, Java Spark (RESTful API)
* **Database:** MongoDB (NoSQL)
* **Frontend:** JavaScript, d3.js, FreeMarker Template Engine
* **NLP Tools:** UIMA, TextImager (SpaCy, GerVader, FastText)

### Project Stages
* **Stage 1 (Assignment 1): Architecture & Parsing** Designed the OOP structure via UML and developed a Java-based parser to process local XML files into structured core data.
* **Stage 2 (Assignment 2): NoSQL Integration** Integrated MongoDB to effectively persist, query, and filter large volumes of speech and speaker records without redundancy.
* **Stage 3 (Assignment 3): NLP Processing** Utilized UIMA pipelines to enrich speech texts with POS tagging, sentiment scores, and DDC topic classifications.
* **Stage 4 (Assignment 4): Web API & Basic Visualization** Expanded the backend with REST endpoints and created interactive frontend charts using d3.js to show sentiment distributions.
* **Final Project: Full System Integration** Collaborated in a team to build the final web application featuring user management, live text editing, interactive networks, and custom PDF generation using $\LaTeX$.
