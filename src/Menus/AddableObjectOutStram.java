package Menus;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class AddableObjectOutStram extends ObjectOutputStream {
    public AddableObjectOutStram(OutputStream out) throws IOException{super(out);}

    protected AddableObjectOutStram() throws IOException, SecurityException{super();}

    protected void writeStreamHeader() throws IOException{}
}
