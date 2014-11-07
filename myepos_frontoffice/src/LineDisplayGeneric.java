/*
 * Copyright (C) 2014 Sunny Patel
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package frontoffice;

import java.io.OutputStream;

/**
 *
 * @author Sunny Patel
 */

//TODO: Maybe implement all of this under a perif class where it can be handled nicely
public class LineDisplayGeneric extends salesWindow {
/*
    CommPort cp;
    OutputStream out;
    SerialReader input;
    CommPortIdentifier portID;
    static int PortInUseInt = 0;
    int flushval = 0;
    
    public LineDisplayGeneric(String port) {
        // We get to know whcih port to open the display at
        try {
            portID.getPortIdentifier(port);
            // Does someone own this yet?
            if(portID.isCurrentlyOwned()) {
                // No cannot use this
            } else {
                // Open the port
                CP = portID.open(this.getClass().getName(), 2000);
                if ( CP instanceof SerialPort ) {
                    SerialPort SP = (SerialPort) CP;
                    SP.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                    
                    InputStream in SP.getInputStream();
                    out = SP.getOutputStream();
                    
                    input = new SerialReader(in);
                    SP.addEventListener(input);
                    SP.notifyOnDataAvailable(true);
                    Thread.sleep(100);
                }
            }
        } catch(Exception a) {
            a.printStackTrace();
        }
    }
    
    public void updateDisplay(String line) {
        
    }
    
    
    public void rebootThing() {
        CP.close();
    }
    */
}
