GHI_UTF_ColonOrderProcessing.createOrder(); 
Order ord = [Select Id, OSM_Created_By_User__c From Order where Id= :$orderId];
System.assert(ord.OSM_Created_By_User__c != Null);
