# Atelier Mecanique WebApplication
External Client Project: Customer, Appointment and Invoice management system

## How to lunch demo on localhost:
- Download Docker
- Git clone the repository on your system
- Uncomment mysql container in docker-compose.yml
- Uncomment application.yml in API
- Comment out lines 17-21 in application.properties
- Use “docker-compose build” to build the project containers
- Use “docker-compose up” to run the containers

# Website deployed on:
       https://ateliermecanique-ws.onrender.com/
       
## Epic Acronyms
-  These acronyms will be used when naming branches and pull requests
### Vehicle Management     
  -  **VM**  
### Invoice Management  
  -  **IM**  
### Customer Account Management  
  -  **CAM**  
### Customer Invoice Management  
  -  **CIM**  
### UI/UX Design
  -  **UI/UX**  
### Database Management  
  -  **DBM**
### Notification Email  
  -  **NE**
### Setup   
  -  **Setup**
### Appointment Management  
  -  **APPM**
### User Authentication and Authorization    
  -  **AUTH**
### Reviews 
  - **REV**
  



## Branch Naming
Branches will be named according to the following convention: type/ACMS-EPIC_ACRONYM-JiraID_Description will be breaken down into  4 'folders' or types:

  feat/  
  bug/  
  doc/  
  conf/  
  

#### Example
feat/ACMS-VM-1-Test-New-Vehicle

## Pull Request Naming  

   To be organised use the following standard for naming PRs  

   type(ACMS-EPIC_ACRONYM-JiraID): PR description



