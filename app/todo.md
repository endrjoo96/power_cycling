[----------------------------------- TODO LIST -----------------------------------]

[---------- LEGEND ----------]
- task waiting to complete
> task implementation in progress
> [task implemented, but not tested]
--- corrections
# task implemented and tested


`________________________v0.1.0________________________`
 > replace devices names with devices enum list (with friendly names)
 # when clicked, only available rooms are shown in right panel
 # when room button is clicked, bundle is booked into the room
 > show current wallet balance in room and device management views
    # stan portfela w ekranie zarzadzania pokojem
    > stan portfela w ekranie ulepszania urządzenia
    - poprawić formatowanie (jest 105.6, powinno być 105,60 - jak w głownym ekranie)
 - make adding devices to rooms
    - should show modal
    - should allow selecting device type, quality and efficiency (spinners?) 
 > [upgrading rooms capacity]
 > upgrading quality of devices
 > fix bugs
    # zapotrzebowanie na prąd nie odświeża się
    # grafika postaci nie odświeża się
    # portfel nie odświeża się podczas upgrade'owania pokoju
    # gotowe urządzenia są tworzone raz i kopiowane przez referencję, należy tworzyć nowe 
      urządzenia przy każdej operacji get()

`________________________v0.1.1________________________`

 - modify devices names enum - use localization feature instead of hard-coded strings
 - implementing cost for upgrades
    - create service for cost calculations
        - should calculate new devices cost (lower than upgrading it from scratch)
        - should calculate rooms and devices upgrades costs
    - add cost to upgrading efficiency of devices
    - add cost to upgrading room capacity
    - add cost to adding devices

`________________________v0.1.2________________________`
 - queue bundle can have their own devices, which affects power consumption 
   (cannot be upgraded)
 - add generating income from rooms  
    - add new tick engine for time cycle
    - queue bundles should have information about time spent in room
    - add unbooking after defined period of time
 - make generated queue (progressively harder)
     - better quality devices requirement increase income from group

`________________________v0.1.3________________________`
- add brands of devices and maximum tiers of upgrading ability (lowend brands should have limit to upgrades)
- make balance of economy, including brands of devices
 - add option to dismiss group from queue (financial loss/maybe some sort of star rating for hotel?)




`_______________________backlog________________________`
 - podczas gry istnieje mala szansa, ze mozliwy bedzie zakup jakiegos urzadzenia po 
  nizszej cenie w wyprzedazy - mozliwe jest wtedy wybranie pokoju, do ktorego to urzadzenie trafi
