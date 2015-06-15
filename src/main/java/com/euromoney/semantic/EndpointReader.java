package com.euromoney.semantic;

import com.sun.corba.se.impl.protocol.ServantCacheLocalCRDBase;

/**
 * A generic endpoint reader. Implementations can be created for different endpoints.
 *
 * Created by Rob Walpole on 10/06/15.
 */
public interface EndpointReader {

    public String extractResult(String resourceName) throws ServiceException;

    public String extractResultWithSparql(String resourceName) throws ServiceException;

}
