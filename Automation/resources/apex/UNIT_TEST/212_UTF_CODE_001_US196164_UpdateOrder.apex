GHI_UTF_ColonOrderProcessing.createOrder(); 
System.assert($orderId <> '');
Order ord = [Select Id, OSM_Created_By_User__c From Order where Id= :$orderId];
ord.OSM_Created_By_User__c = null;
try
{
	update ord;
	System.debug('update 1');
}
catch(Exception e)
{
	System.debug('update 1'+e.getMessage());
	System.assert(e.getMessage().contains('Order Created By User must be populated.'));
}


