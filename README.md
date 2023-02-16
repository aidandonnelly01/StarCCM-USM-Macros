# USM Macros for Siemens' Simcenter StarCCM+

Documentation is currently a work in progress, however these macros should give a good idea on how to create and run macros designed 
for use in FSAE aerodynamic simulations

Some adaptation may be required to make these sims work with other team's CAD models, however these macros assume that the sim has 
been meshed and ran correctly, and that the origin point of the car should be between the front two wheels. 

Should be designed to work with half car or full car CAD models

# Running the macro

Download the file ExperterV2.java and run in StarCCM+'s macro suite. No compiler should be nessecary.

#Additional information

Scattered throughout the project are various failed attempts at macros and/or helper macros that I wrote to understand the how the
macro suite of StarCCM+ works, there is a Java API provided with installations of StarCCM+ as well as a Java SDK (Java 11) and a Java 
libary containing all classes/methods the macros will use. I would recommend writing the macros in a proper IDE such as IntelliJ, which is 
my personal preference due to how easy it is to work with external libraries using this IDE. Other IDEs such as VSCode or even Notepad++ 
should work for basic highlighting/syntax reporting. Please do not use basic ass notepad.

Additionally, I found the resources provided by the Otto von Guericke University Magdeburg to be very helpful, some snippets of code are 
inspired by their tutorials

link: https://wikis.ovgu.de/lss/doku.php?id=guide:starccm

# Contributors 

Oran Brockie, member of Aerodynamics @USM

James Baptie, system head of Cooling @USM

Andrew Smith, system head of Aerodynamics @USM

Aidan Donnelly, member of Cooling/Aero @USM, Primary project software engineer
