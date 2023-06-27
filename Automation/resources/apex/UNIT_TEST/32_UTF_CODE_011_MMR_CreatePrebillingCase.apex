GHI_UTF_MMROrderProcessing.createOrder();
GHI_UTF_MMROrderProcessing.createOrderRole('Ordering');
GHI_UTF_MMROrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_MMROrderProcessing.processOrder($orderId); 
List<Case> prebilling = [Select Id from Case where Type = 'Pre-Billing' and OSM_Primary_Order__c= :$orderId];
System.assert(prebilling.size()>0);