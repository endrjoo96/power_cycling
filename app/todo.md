[----------------------------------- TODO LIST -----------------------------------]

`________________________v0.1.0________________________`
 # replace devices names with devices enum list (with friendly names)
 # when clicked, only available rooms are shown in right panel
 # when room button is clicked, bundle is booked into the room
 > [show current wallet balance in room and device management views]
 - make adding devices to rooms
    - should show modal
    - should allow selecting device type, quality and efficiency (spinners?) 
 > [upgrading rooms capacity]
 > upgrading quality of devices

`________________________v0.1.1________________________`
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
    - better quality devices increase room income 
 - make generated queue (progressively harder)

`________________________v0.1.3________________________`


`_______________________backlog________________________`
 - podczas gry istnieje mala szansa, ze mozliwy bedzie zakup jakiegos urzadzenia po 
  nizszej cenie w wyprzedazy - mozliwe jest wtedy wybranie pokoju, do ktorego to urzadzenie trafi



[---------- LEGEND ----------]
 - task waiting to complete
 > task implementation in progress
 > [task implemented, but not tested]
 # task implemented and tested