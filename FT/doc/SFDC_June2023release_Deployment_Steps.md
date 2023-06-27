# SFDC Deployment Instructions [June2023release]

# 1 Pre-Ant DataLoad
None

# 2 Pre-ANT Pre-Apex Manual Steps
None

# 3 Pre-Ant Apex Scripts
Yes

# 4 Pre-Ant Post-Apex Manual Steps
None

# 5 ANT Destructive Package (Pre-Deployment)
None

# 6 ANT Deployment Package
Yes

# 7 Profile Deployment
Yes

# 8 ANT Destructive Package (Post-Deployment)
None

# 9 Post-ANT Data Load
None

# 10 Post-ANT Pre-Apex Manual Steps
None

# 11 Post-ANT Apex Scripts
Yes

# 12 Post-ANT Post-Apex Manual Steps
None

# 13 Post-ANT Guides/Process Deployment
## 1 Synchronize IICS with most recent SFDC metadata
1. Login into IICS Cloud Server with url: https://dm-us.informaticacloud.com/identity-service/home
2. Navigate to the card named as **Application Integration**, click **Explore** tab on left panel, and click on **Default** link. All IICS components are displayed on redirected page.
3. Search connection `Salesforce` from list using find box.
4. Click on three dots appears at the end of row at on mouse hover on `Salesforce` connection row.
5. Now Click on **Publish**.
6. Alert will be shown at the top if Salesforce connection Published successfully. If message shown in below screenshot appear then wait for some time as the process is publishing in background.

**Note**: We can check the status of connection by clicking at three dots appears at the end of row and then click on 'Properties Details...'

## 2 Import IICS Service Connector
 
** For `GHI-CRM-Outbound-Shilo-REST' Service Connectors:-**
 
1. Download the zip files IICS\\**IICSConnectors.zip** from Build folder containing Guides and Processes, Process Object to be imported. 
2. Click on Import button on top right
3. Click Choose File. Locate and choose the zip files under build folder (**IICSConnectors.zip**)
4. Click on Type column to sort the list, search and **select** all rows with Type as (“Service connector” and “Connection”) from list of item under Select Assets
5. Select the following: 
    - Check the **Overwrite existing assets**.
    - Select **Target Project 1 equal to Default** (appears under 'preview locations' section)
    - Check **Publish Application Integration assets after import** checkbox under Application Integration Options.
6. Click **Import** button displayed on top right to import all components together

## 3 Import IICS Service Connections
 
** For `GHI-ESB-Shilo-REST' Service Connections:-**
 
1. Download the zip files IICS\\**IICSConnections.zip** from Build folder containing Guides and Processes, Process Object to be imported. 
2. Click on Import button on top right
3. Click Choose File. Locate and choose the zip files under build folder (**IICSConnections.zip**)
4. Click on Type column to sort the list, search and **select** all rows with Type as (“Service connector” and “Connection”) from list of item under Select Assets
5. Select the following: 
    - Check the **Overwrite existing assets**.
    - Select **Target Project 1 equal to Default** (appears under 'preview locations' section)
    - Check **Publish Application Integration assets after import** checkbox under Application Integration Options.
6. Click **Import** button displayed on top right to import all components together

## 4 Import IICS Processes, Guides and Process Object
1. Download the zip files IICS\\**IICSComponentsProcessObjects.zip** from **Build** folder containing Guides and Processes, Process Object to be imported.
2. Login into IICS Cloud Server, click Card named as **Application Integration**, click **Explore** tab on left panel, click on **Default** link. All IICS components are displayed on redirected page.
3. Click on **Import** button on top right
4. Click **Choose File**. Locate and choose the zip files under build folder (**IICSComponentsProcessObjects.zip**)
5. Click on **Type** column to sort the list, search and **unselect** all rows with Type as (“Service connector” and “Connection”) from list of item under **Select Assets**
6. Select the following:
    - Check the **Overwrite existing assets, excluding connections and runtime environments** checkbox.
    - Select **Target Project 1** equal to **Default** (appears under ‘preview locations’ section)
7. Click **Import** button displayed on top right to import all components together

## 5 Import IICS Processes, Guides and Process Object
1. Download the zip files IICS\\**IICSComponents.zip** from **Build** folder containing Guides and Processes, Process Object to be imported.
2. Login into IICS Cloud Server, click Card named as **Application Integration**, click **Explore** tab on left panel, click on **Default** link. All IICS components are displayed on redirected page.
3. Click on **Import** button on top right
4. Click **Choose File**. Locate and choose the zip files under build folder (**IICSComponents.zip**)
5. Click on **Type** column to sort the list, search and **unselect** all rows with Type as (“Service connector” and “Connection”) from list of item under **Select Assets**
6. Select the following:
- Check the **Overwrite existing assets, excluding connections and runtime environments** checkbox.
- Select **Target Project 1** equal to **Default** (appears under ‘preview locations’ section)
- Check **Publish Application Integration assets after import** checkbox under **Application Integration Options**.
7. Click **Import** button displayed on top right to import all components together

## 6 Import IICS Processes, Guides and Process Object
1. Download the zip files IICS\\**IICSComponentsWithAccessSettings.zip** from **Build** folder containing Guides and Processes, Process Object to be imported.
2. Login into IICS Cloud Server, click Card named as **Application Integration**, click **Explore** tab on left panel, click on **Default** link. All IICS components are displayed on redirected page.
3. Click on **Import** button on top right
4. Click **Choose File**. Locate and choose the zip files under build folder (**IICSComponentsWithAccessSettings.zip**)
5. Click on **Type** column to sort the list, search and **unselect** all rows with Type as (“Service connector” and “Connection”) from list of item under **Select Assets**
6. Select the following:
- Check the **Overwrite existing assets, excluding connections and runtime environments** checkbox.
- Select **Target Project 1** equal to **Default** (appears under ‘preview locations’ section)
7. Click **Import** button displayed on top right to import all components together

## 7 Update Access Settings for BizTalk ICRT user
1. Login into IICS Cloud Server.
2. Click Card named as **Application Integration**.
3. Click **Explore** tab on left panel.
4. Now click on **Default** link.
5. All IICS components are displayed on redirected page.
6. **For each** of the Processes listed below, execute the following steps:
7. Within the listing, double click the Process
8. Click the **Start** tab on the right menu
9. Make sure **Allow Anonymous Access** is unchecked
10. On **Allowed Users** field, add the BizTalk ICRT user specific to the environment (E.g. `biztalk@dev14.ghi`, `biztalk@genomichealth.com`)  
    **Note**: Make sure the respective environment user is present and remove all other user id’s which are not related to IICS environment. 
11. Click **Save** and then **Publish**.

    Processes:
    - `INT-MP-QueryMessage`
	- `INT-MP-SpecimenMessage`
	- `INT-MP-CaseDataMessage`
	- `INT-MP-ResultsReadyData-Message`
	- `INT-MP-SpecimenResultMessage`
	- `INT-MP-DistributionEvent-Message`
	
## 8 Update Access Settings for Starlims ICRT user
1. Login into IICS Cloud Server.
2. Click Card named as **Application Integration**.
3. Click **Explore** tab on left panel.
4. Now click on **Default** link.
5. All IICS components are displayed on redirected page.
6. **For each** of the Processes listed below, execute the following steps:
7. Within the listing, double click the Process
8. Click the **Start** tab on the right menu
9. Make sure **Allow Anonymous Access** is unchecked
10. On **Allowed Users** field, add the Starlims ICRT user specific to the environment (E.g. `icrtstarlimsintegration@dev14.ghi`, `icrtstarlimsintegration@genomichealth.com`)  
    **Note**: Make sure the respective environment user is present and remove all other user id’s which are not related to IICS environment. 
11. Click **Save** and then **Publish**.

    Processes:
	- `MPPathDataRetrieval`
	- `MPDataRetrievalForLIMS`

## 9 Update Access Settings for Riskguard ICRT user
1. Login into IICS Cloud Server.
2. Click Card named as **Application Integration**.
3. Click **Explore** tab on left panel.
4. Now click on **Default** link.
5. All IICS components are displayed on redirected page.
6. **For each** of the Processes listed below, execute the following steps:
7. Within the listing, double click the Process
8. Click the **Start** tab on the right menu
9. Make sure **Allow Anonymous Access** is unchecked
10. On **Allowed Users** field, add the Riskguard ICRT user specific to the environment (E.g. `icrtpgintegration@dev14.ghi`, `icrtpgintegration@genomichealth.com`)  
    **Note**: Make sure the respective environment user is present and remove all other user id’s which are not related to IICS environment. 
11. Click **Save** and then **Publish**.

    Processes:
	- `INT-MP-Order-Information-Retrieval`
	
