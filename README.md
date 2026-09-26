<h1 align="center">🎬 Movie Management & Streaming Platform</h1>

<div align="center">
  <img src="https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java" alt="Java" />
  <img src="https://img.shields.io/badge/JavaFX-GUI-blue?style=for-the-badge" alt="JavaFX" />
  <img src="https://img.shields.io/badge/Oracle-PL%2FSQL-red?style=for-the-badge&logo=oracle" alt="Oracle" />
  <img src="https://img.shields.io/badge/Architecture-N--Tier-brightgreen?style=for-the-badge" alt="Architecture" />
</div>

<p align="center">
  <em>A comprehensive, enterprise-structured desktop application simulating a movie streaming and management ecosystem.</em>
</p>

<p>
  This project demonstrates the seamless integration of a rich <strong>JavaFX Client</strong> with a highly optimized, normalized <strong>Oracle SQL Database</strong>. Built with a focus on scalability and performance, the application implements industry standards such as <strong>N-Tier Architecture (Controllers, Services, DAOs)</strong>, <strong>Connection Pooling (HikariCP)</strong>, and strict <strong>Separation of Concerns</strong>.
</p>

<hr>

<h2>✨ Core Features</h2>
<ul>
  <li><strong>Dynamic Catalog & High-Performance Filtering:</strong> Users can search movies by title or filter by genre. The backend utilizes Java Streams for efficient in-memory data processing, guaranteeing rapid UI updates without overloading the database.</li>
  <li><strong>Interactive Playback Simulation:</strong> Features a dynamic viewing engine where users select audio languages and video formats. A custom mathematical algorithm calculates the exact watched minutes based on user behavior, automatically updating the viewing status.</li>
  <li><strong>Automated Review & Sentiment System:</strong> A fully interactive review system integrated directly with Oracle PL/SQL. Database triggers automatically recalculate the movie's global average rating in real-time and assign a sentiment score (Positive, Neutral, Negative) to user comments.</li>
  <li><strong>Personalized Recommendation Engine:</strong> Analyzes the user's specific viewing history through complex SQL queries to generate personalized movie recommendations on the dashboard.</li>
</ul>

<hr>

<h2>📸 Application Showcase</h2>

<h3>1. User Authentication</h3>
<div align="center">
  <img src="Login.png" alt="Login Screen" width="700"/>
</div>

<h3>2. Main Dashboard & Catalog</h3>
<div align="center">
  <img src="Dashboard.png" alt="Dashboard Screen" width="700"/>
</div>

<h3>3. Movie Details & Interactions</h3>
<div align="center">
  <img src="MovieDetails1.png" alt="Movie Details Screen 1" width="700"/>
  <img src="MovieDetails2.png" alt="Movie Details Screen 2" width="700"/>
  <img src="MovieDetails3.png" alt="Movie Details Screen 3" width="700"/>
</div>

<hr>

<h2>🏛️ Architecture & Tech Stack</h2>
<p>The application is engineered using a modern <strong>N-Tier Architecture</strong>, ensuring the User Interface is completely decoupled from the data access logic.</p>
<ul>
  <li><strong>Presentation Layer (JavaFX):</strong> Lightweight Controllers dedicated exclusively to UI event handling and rendering.</li>
  <li><strong>Business Layer (Services):</strong> Centralized logic classes (e.g., <code>CatalogService</code>, <code>MovieDetailsService</code>) that enforce business rules, handle complex calculations, and format data for the client.</li>
  <li><strong>Persistence Layer (DAO & JDBC):</strong> Data Access Objects responsible for secure and optimized database interactions.</li>
  <li><strong>Infrastructure (HikariCP):</strong> Advanced connection pooling implementation that manages database connections efficiently, preventing resource exhaustion. Database credentials are securely externalized into a <code>.properties</code> file.</li>
</ul>

<hr>

<h2>🗄️ Database Design</h2>

<p>The database acts as an active component of the business logic, utilizing advanced PL/SQL features:</p>
<ul>
  <li><strong>Compound Triggers:</strong> Designed to prevent mutating table errors while automatically recalculating a movie's <code>rating_mediu</code> across the entire catalog upon any review modification.</li>
  <li><strong>Business Logic Automation:</strong> Triggers that parse review text to assign a <code>sentiment_score</code> and update a view's status to "COMPLETED" automatically when watched minutes match the total runtime.</li>
  <li><strong>Automated Seeding:</strong> A sophisticated PL/SQL script utilizing <code>VARRAY</code> collections and loops to bulk-insert highly relational seed data (movies, cast, user histories) instantly.</li>
</ul>

<hr>

<h2>🚀 Core Competencies Demonstrated</h2>
<ul>
  <li><strong>Enterprise Software Architecture:</strong> Implementation of clean N-Tier architecture with strict Separation of Concerns.</li>
  <li><strong>Advanced Database Engineering:</strong> Application of relational algebra (BCNF, 4NF) to design scalable enterprise schemas.</li>
  <li><strong>Performance Optimization:</strong> Utilization of Java Streams and memory-efficient data structures.</li>
  <li><strong>Secure Infrastructure:</strong> Implementation of connection pooling and externalized configuration management.</li>
</ul>

<hr>

<h2>⚙️ Getting Started</h2>

<h3>Prerequisites</h3>
<ul>
  <li>Java Development Kit (JDK) 21 or higher.</li>
  <li>Oracle Database (XE or Enterprise).</li>
  <li>Maven (for dependency management).</li>
</ul>

<h3>Installation & Setup</h3>
<ol>
  <li><strong>Clone the repository:</strong>
    <pre><code>git clone https://github.com/yourusername/movie-streaming-platform.git
cd movie-streaming-platform</code></pre>
  </li>
  <li><strong>Configure the Database:</strong>
    <ul>
      <li>Navigate to <code>src/main/resources/</code>.</li>
      <li>Rename <code>application.properties.example</code> to <code>application.properties</code>.</li>
      <li>Update the credentials to match your local Oracle instance:
        <pre><code>db.url=jdbc:oracle:thin:@localhost:1521:xe
db.username=your_user
db.password=your_password
db.pool.size=32</code></pre>
      </li>
    </ul>
  </li>
  <li><strong>Initialize the Schema:</strong>
    <p>Execute the scripts found in the <code>database/</code> folder in the following order:</p>
    <ul>
      <li><code>1_schema.sql</code> (Creates tables, sequences, and constraints)</li>
      <li><code>2_data.sql</code> (Populates the database with initial seed data)</li>
      <li><code>3_plsql_logic.sql</code> (Compiles functions and triggers)</li>
    </ul>
  </li>
  <li><strong>Run the Application:</strong>
    <p>Execute the main class <code>MoviePlatformApplication.java</code> from your IDE.</p>
  </li>
</ol>
