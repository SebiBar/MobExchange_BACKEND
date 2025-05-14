# Testarea aplicației

Pentru pornirea backend-ului, se utilizează comanda ./gradlew bootRun (în mediile Windows), executată din directorul rădăcină al proiectului. Înainte de lansare, este necesară configurarea variabilelor de mediu aferente cheilor API în fișierul .env, situat în rădăcina proiectului. Aceste variabile trebuie definite după cum urmează:
RAPID_API_KEY=valoarea_cheii
NEWS_API_KEY=valoarea_cheii

În ceea ce privește frontend-ul, procesul presupune navigarea în directorul dedicat aplicației frontend și rularea comenzilor npm install, pentru instalarea tuturor dependențelor necesare, urmată de npm start, care inițiază serverul de dezvoltare al frontend-ului.

După parcurgerea acestor pași, backend-ul va fi accesibil pe portul configurat în setările proiectului, în timp ce frontend-ul va fi disponibil pe portul implicit utilizat de cadrul de lucru, cel mai frecvent 5173. Această separare asigură funcționarea corectă și independentă a celor două componente ale aplicației.
