/**
 * 
 */
package com.admtel.telephonyserver.asterisk;

/**
 * @author danny
 * A Class that retrieves a list of variables from asterisk (in an asynchronous way)
 * The problem with asterisk is that it doesn't report enough information about the
 * call. Most of the information can be retrieved using a getVar call, however, this methods
 * is asynchronous, making the state machine complicated if we want to retrieve more than one variable.  
 *
 */
public class ASTVariableFetcher {

}
