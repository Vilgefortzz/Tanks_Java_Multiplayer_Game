TANKS - MULTIPLAYER
-------------------

Autor: Grzegorz Klimek Gr. Lab. 4 rok II IS WIMIiP
--------------------------------------------------

Opis projektu: 

Projekt ten przewiduje stworzenie gry, w której użytkownicy będą się rejestrować w bazie danych, 
podając tzw. nick gracza. Później muszą się zalogować - bez tego niemożliwe jest dołączenie do gry.
Statystyki wszystkich zarejestrowanych użytkowników będą dostępne w menu.
Następnie taki użytkownik może wejść do gry dostając do dyspozycji czołg wygenerowany na mapie. 
Gracz porusza się czołgiem a jego zadanie polega na zniszczeniu wrogich czołgów(innych graczy, którzy 
połączą sie z serwerem i wejdą do gry).

---------------------------------------------------------------------------------------------------------------------

Harmonogram:


[1] Okienka tworzone przy pomocy biblioteki graficznej Swing

[1.1] Stworzenie klasy obsługującej główne okno programu (klasa GUI)

	-> stworzenie okna, odpowiednie ustawienie go na ekranie, nazwanie itp.
	-> stworzenie panelu odpowiedzialnego za ustawienie tła aplikacji (klasa MyPanel)
	-> ustawienie icona okna aplikacji

[1.2] Tworzenie GUI + oprogramowanie przycisków

	-> utworzenie menu głównego aplikacji wraz z menu pobocznymi
	-> stworzenie buttonów, napisów, buttonów wyboru, textfieldów
	-> działanie bez konieczności otwierania nowych okien - wszystko w jednym oknie
	-> dodanie akcji do przycisków po kliknięciu myszą

[1.3] Wygenerowanie mapy oraz stworzenie modelu czołgu + oprogramowanie go

	-> stworzenie mapy przy pomocy odpowiedniej klasy (klasa MapPanel)
	-> dodanie belki na górze mapy z informacją o aktualnym życiu, ilości zniszczonych czołgów i bycia zniszczonym
	-> stworzenie czołgu przy pomocy odpowiedniej klasy (klasa Tank)
	-> losowe generowanie czołgu na mapie
	-> sterowanie czołgiem - reakcja na klawisze
	-> strzelanie czołgiem

[1.4] Stworzenie dodatkowych okien interfejsu

	-> utworzenie interfejsu serwera

[1.5] Wygląd i zachowanie przeciwnych czołgów

    -> prawidłowe zachowanie się czołgu czyli obracanie się (animacja), kierunek strzału
    -> poprawna fizyka gry
	-> mechanika związana z otrzymywanymi obrażeniami
	-> zachowanie czołgów - utrata zdrowia, sprawdzanie czy został zniszczony, jeśli tak to usuwanie go z mapy

---------------------------------------------------------------------------------------------------------------------

[2] Zapis i odczyt plików

[2.1] Zapis do pliku wszystkich zarejestrowanych użytkowników
	
	-> forma listy:

		LICZBA GRACZY: ...
		1) Greg
		2) Stefan
		...

[2.2] Zapis do pliku statystyk konkretnego gracza
	
	-> forma listy:

		Moje Statystyki:
		GRACZ: login
		Liczba zniszczonych czołgów: ...
		Zostałeś zniszczony : ... razy
		
[2.3] Plik z logami klienta (klient)

[2.4] Plik z logami serwera (serwer)

---------------------------------------------------------------------------------------------------------------------

[3] Współbieżność: wątki, operacje atomowe, itp.

[3.1] Utworzenie głównego wątku programu

	-> obsługa zapisu do pliku

[3.2] Utworzenie wątku liczącego zniszczone czołgi i bycie zniszczonym

[3.3] Ruch gracza + kolizje

[3.4] Utworzenie wątku serwera

---------------------------------------------------------------------------------------------------------------------

[4] Bazy danych: JDBC dla: MySQL

[4.1] Podstawowa konfiguracja JBDC

[4.2] Obsługa bazy danych (aplikacja serwera)

	-> łączenie z bazą danych
	-> tworzenie tabel
	-> wstawianie danych do bazy
	-> wczytywanie danych z bazy do listy
	-> zamykanie połączenia

[4.3] Utworzenie klasy DataToBase (aplikacja serwera)

	-> zbiór zmiennych przechowujących dane do zapisu/odczytu z bazy danych
	-> zbiór funkcji wczytujących wartości (get)
	-> zbiór funkcji wstawiających wartości (set)
	-> funkcja wyświetlająca

[4.4] Aktualizowanie bazy danych

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

[5.4] Połączenie za pomocą adresu IP

---------------------------------------------------------------------------------------------------------------------

[6] Zaproponowane przez studenta

[6.1] Wzorzec projektowy - singleton

[6.2] Grafika

	-> dodanie odpowiednich grafik związanych z tematyką gry
	-> utworzenie przejrzystego menu głównego aplikacji
	-> zmiana stylu buttonów na przyjemny dla wzroku
	
[6.3] Język interfejsu

	-> zmiana języka w grze (Angielski lub Polski) wybierane w menu głównym w opcjach gry

[6.4] Połączenie z zewnętrzną bazą danych

---------------------------------------------------------------------------------------------------------------------

[Legenda]

[1] - poruszane tematy
[1.1] - zadania
[->] - podzadania

---------------------------------------------------------------------------------------------------------------------