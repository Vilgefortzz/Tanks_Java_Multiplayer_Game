**TANKS - MULTIPLAYER**
---------------------------------------------------------------------------------------------------------------------

_Autor: Grzegorz Klimek Gr. Lab. 4 rok II IS WIMIiP_
--------------------------------------------------

Opis projektu: 

Projekt ten przewiduje stworzenie gry, w której użytkownicy będą się rejestrować w bazie danych, 
podając tzw. nick gracza. Później muszą się zalogować - bez tego niemożliwe jest dołączenie do gry.
Statystyki wszystkich zarejestrowanych użytkowników będą dostępne w menu.
Następnie taki użytkownik może wejść do gry dostając do dyspozycji czołg wygenerowany na mapie. 
Gracz porusza się czołgiem a jego zadanie polega na zniszczeniu wrogich czołgów (innych graczy, którzy
połączą sie z serwerem i wejdą do gry).

---------------------------------------

**_Harmonogram:_**


[1] Okienka tworzone przy pomocy biblioteki graficznej Swing

[1.1] ~~Stworzenie klasy obsługującej główne okno programu (klasa GUI)~~ [ 08.03.16 ]

	-> stworzenie okna, odpowiednie ustawienie go na ekranie, nazwanie itp.
	-> stworzenie panelu odpowiedzialnego za ustawienie tła aplikacji (klasa MyPanel)
	-> ustawienie icona okna aplikacji

[1.2] ~~Tworzenie GUI + oprogramowanie przycisków~~ [ 08.03.16 ]

	-> utworzenie menu głównego aplikacji wraz z menu pobocznymi
	-> stworzenie buttonów, napisów, buttonów wyboru, textfieldów
	-> działanie bez konieczności otwierania nowych okien - wszystko w jednym oknie
	-> dodanie akcji do przycisków po kliknięciu myszą

[1.3] ~~Wygenerowanie mapy oraz stworzenie modelu czołgu + oprogramowanie go~~ [ 15.03.16 ]

	-> stworzenie mapy (tylko tło) przy pomocy odpowiedniej klasy (klasa MapPanel)
	-> dodanie belki na górze mapy z informacją o aktualnym życiu, ilości zniszczonych czołgów i bycia zniszczonym
	-> stworzenie czołgu przy pomocy odpowiedniej klasy (klasa Player)
	-> sterowanie czołgiem - reakcja na klawisze
	-> strzelanie czołgiem - w jednym kierunku

[1.4] ~~Animacje czołgów~~ [ 22.03.16 ]

    -> prawidłowe zachowanie się czołgu czyli obracanie się (animacja), kierunek strzału
    -> losowe generowanie czołgu na mapie

[1.5] ~~Rozgrywka ( otrzymywanie obrażeń, strata zdrowia, usuwanie z mapy )~~ [ 05.04.16 ]

    -> poprawna fizyka gry
	-> mechanika związana z otrzymywanymi obrażeniami
	-> zachowanie czołgów - utrata zdrowia, sprawdzanie czy został zniszczony, jeśli tak to usuwanie go z mapy

---------------------------------------------------------------------------------------------------------------------

[2] Zapis i odczyt plików

[2.1] Zapisywanie i odpisywanie konfiguracji

    -> na jakim porcie jest rozgrywka, ile czołgów, użytkownik który łączy sie na serwerze
    -> konfiguracja serwera i klienta

[2.2] ~~Wczytywanie mapy~~ [ 22.03.16 ]
		
[2.3] Plik z logami klienta i serwera (klasa do obsługi logów z przed 2 tygodni np.)

[2.4] Zapis i wczytywanie stanu gry

---------------------------------------------------------------------------------------------------------------------

[3] Współbieżność: wątki, operacje atomowe, itp.

[3.1] Mechanizm bezpiecznego rozgłaszania komunikatów między użytkownikami

[3.2] ~~Ruch gracza + kolizje (wątek animacji)~~ [ 05.04.16 ]

[3.4] Utworzenie wątku serwera(akceptujący) + wątki dla każdego klienta + mechanizm bezpiecznego zatrzymywania wątku(join, flagi podtrzymujące)

[3.5] Wątek do każdego klienta (synchronizowanie obiektów i bezpieczne przerwania)

---------------------------------------------------------------------------------------------------------------------

[4] Bazy danych: JDBC dla: MySQL

[4.1] Zaprojektowanie bazy danych użytkowników, połączenie z bazą za pomocą JDBC(min. 3 tabele)

    -> łączenie z bazą danych
    -> wstawianie danych do bazy
    -> wczytywanie danych z bazy do listy
    -> zamykanie połączenia

[4.2] Mechanizm walidacji (regexy - java regexy + siła hasła)

[4.3] Logika autoryzacji

    -> logowanie
    -> rejestracja

[4.4] Mechanizm obsługi statystyk (raport - w ciągu ostatniego miesiąca jakiś tam uzytkownik miał jakieś tam statystyki)

---------------------------------------------------------------------------------------------------------------------

[5] Komunikacja sieciowa poprzez sockety

[5.1] Aplikacja server

	-> oczekiwanie na połączenie
	-> odbieranie informacji od klienta
	-> wykonanie operacji w zależności od flagi przekazanej przez główną aplikację.

[5.2] Aplikacja client

	-> łączenie się z serwerem
	-> wysyłanie komunikatów do serwera i oczekiwanie na odpowiednią odpowiedź w zależności od wysłanej flagi.
	-> wykonanie odpowiedniej operacji w zależności od wysłanej informacji zwrotnej od serwera.

[5.3] Ustawienie połączenia wielu klientów poprzez serwer

[5.4] Protokół komunikacji DataInputStream, DataOutputStream

---------------------------------------------------------------------------------------------------------------------

[6] Zaproponowane przez studenta

[6.1] ~~Logika rozgrywki, kolizje, timer będzie używał wątku animacji~~ [ 19.04.16 ]

[6.2] ~~Obsługa pocisków~~ [ 19.04.16 ]

[6.2] ~~Grafika~~ [ 15.03.16 ]

	-> dodanie odpowiednich grafik związanych z tematyką gry
	-> utworzenie przejrzystego menu głównego aplikacji
	-> zmiana stylu buttonów na przyjemny dla wzroku

---------------------------------------------------------------------------------------------------------------------

[Legenda]

[1] - poruszane tematy
[1.1] - zadania
[->] - podzadania

---------------------------------------------------------------------------------------------------------------------