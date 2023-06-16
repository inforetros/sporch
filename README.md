# Welcome to Sporch!

Sporch is a Text-based Retrieval System for a Spotify Playlist published on kaggle.com. The project utilizes Apache Lucene, a powerful open-source search library, as a fundamental component. By leveraging the capabilities of Apache Lucene, the system benefits from advanced search and indexing functionalities, enabling efficient retrieval and ranking of songs based on user queries. Apache Lucene provides a robust foundation for text-based information retrieval, allowing the project to deliver accurate and relevant search results within the vast dataset of 1 million playlists.

In order to run the project as a comman line tool in a Windows environment, create a .bat file, namely **sporch.bat** with the following content:
```sh
java -jar playlist-explorer.jar %*
```
For linux platforms, **sporch.sh** file help with the similar content but just have to replace %* with "$@". i.e. the file should have only the following line:
```sh
java -jar playlist-explorer.jar "$@"
```
Use **sporch** as an alias for starting playlist-explorer.

# Dependencies

```sh
<apache.lucene.version>8.10.1</apache.lucene.version>  
<gson.version>2.8.5</gson.version>  
<picocli.version>4.7.4</picocli.version>
```

## Apache Lucene
Apache Lucene was chosen as the underlying search engine for this project due to its renowned capabilities in information retrieval and text indexing. It is a widely adopted and well-established open-source library known for its speed, scalability, and accuracy in handling large volumes of textual data. 

One of the key reasons for selecting Apache Lucene is its extensive support for advanced indexing techniques, including tokenization, stemming, and stop word elimination. These features are essential for efficiently processing and organizing the song titles and track names within the playlist dataset, enabling more precise and effective search results.

Additionally, Apache Lucene provides a flexible and customizable framework for ranking and scoring search results. This allows us to incorporate our specific requirements, such as ranking songs based on their occurrences within playlists. By leveraging Lucene's scoring models and ranking algorithms, we can effectively prioritize and present the most relevant songs to users based on their search queries.

Moreover, the active development community and the availability of comprehensive documentation make Apache Lucene a reliable choice for this project. It offers continuous updates, bug fixes, and community support, ensuring the system's stability and longevity.

Overall, the use of Apache Lucene empowers the project with robust search capabilities, efficient indexing, and customizable ranking methods, making it an ideal choice for developing a text-based information retrieval system for searching songs within the vast playlist dataset.

## Gson
The Gson library was selected for this project to facilitate the parsing and handling of JSON-formatted data. As the dataset of playlists obtained from Kaggle.com is provided in JSON format, Gson provides a convenient and efficient way to extract and process the relevant information.

Gson is a popular Java library developed by Google, specifically designed for working with JSON data. It offers a simple yet powerful API that allows for seamless conversion between Java objects and JSON representations. With Gson, we can easily parse the JSON dataset into meaningful Java objects, enabling us to access and manipulate the playlist data with ease.

By utilizing Gson, we eliminate the need for manual parsing and manipulation of JSON data, saving development time and effort. The library automatically handles the serialization and deserialization of JSON objects, making it straightforward to extract the necessary information from the playlist dataset and incorporate it into our information retrieval system.

Furthermore, Gson provides robust error handling and validation mechanisms, ensuring the integrity and consistency of the parsed JSON data. It offers features like data binding, custom adapters, and flexible configuration options, allowing us to tailor the parsing process to our specific needs.

In summary, the integration of the Gson library into the project simplifies the parsing and handling of JSON-formatted playlist data obtained from Kaggle.com. It enhances the efficiency and reliability of processing the dataset, enabling seamless integration of the playlist information into the information retrieval system.

## Picocli

Picocli is also utilized in our project to facilitate the development of the client user interface. It offers a convenient and efficient way to parse command-line arguments and create interactive command-line interfaces for users. By leveraging the features of Picocli, we can ensure a seamless and user-friendly experience when interacting with our information retrieval system.
