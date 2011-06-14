// =========================================================================== //
//                                                                             //
// Copyright 2011 Anton Dubrau and McGill University.                          //
//                                                                             //
//   Licensed under the Apache License, Version 2.0 (the "License");           //
//   you may not use this file except in compliance with the License.          //
//   You may obtain a copy of the License at                                   //
//                                                                             //
//       http://www.apache.org/licenses/LICENSE-2.0                            //
//                                                                             //
//   Unless required by applicable law or agreed to in writing, software       //
//   distributed under the License is distributed on an "AS IS" BASIS,         //
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  //
//   See the License for the specific language governing permissions and       //
//  limitations under the License.                                             //
//                                                                             //
// =========================================================================== //

package natlab.toolkits.filehandling.genericFile;

import java.io.File;

/**
 * provides some matlab related helpers using Generic Files
 */


public class GenericFileMatlabTools {
    /**
     * represents a file filter matching on matlab files
     */
    static final public GenericFileFilter MATLAB_FILE_FILTER = new GenericFileFilter() {
        public boolean accept(GenericFile file) {
            return file.getExtension().equalsIgnoreCase("m");
        }
    };
    
    static final public GenericFileFilter PRIVATE_DIRECTORY_FILTER = new GenericFileFilter() {
        public boolean accept(GenericFile file)
        {
            return file.isDir() && file.getName().toLowerCase().equals("private");
        }
    };
    
    static final public GenericFileFilter NON_PRIVATE_DIRECTORY_FILTER = new GenericFileFilter() {
        public boolean accept(GenericFile file)
        {
            return file.isDir() && !file.getName().toLowerCase().equals("private");
        }
    };
    
    static final public GenericFileFilter PACKAGE_FILTER = new GenericFileFilter() {
        public boolean accept(GenericFile file)
        {
            return file.isDir() && (file.getName().charAt(0) == '+');
        }
    };

    static final public GenericFileFilter OBJECT_DIRECTORY_FILTER = new GenericFileFilter() {
        public boolean accept(GenericFile file)
        {
            return file.isDir() && (file.getName().charAt(0) == '@');
        }
    };
    
}
