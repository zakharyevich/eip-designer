/*
 * Licensed to Laurent Broudoux (the "Author") under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Author licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.github.lbroudoux.dsl.eip.parser.camel.ui.actions;

import org.eclipse.core.resources.IFile;

import com.github.lbroudoux.dsl.eip.EIPModel;
import com.github.lbroudoux.dsl.eip.EipFactory;
import com.github.lbroudoux.dsl.eip.Route;
import com.github.lbroudoux.dsl.eip.parser.camel.CamelJavaFileParser;
import com.github.lbroudoux.dsl.eip.parser.camel.CamelXmlFileParser;
import com.github.lbroudoux.dsl.eip.parser.core.ui.actions.AbstractCompareWithRouteActionHandler;

/**
 * Handler concrete class for "Compare With EIP Route..." on Apache Camel files.
 * @author laurent
 */
public class CompareWithRouteActionHandler extends AbstractCompareWithRouteActionHandler {

   /** The constructor. */
   public CompareWithRouteActionHandler() {
      
   } 
   
   @Override
   protected Route extractRouteFromFile(IFile selectionFile) {
      // Initialize empty model to be fill by appropriate parser.
      EIPModel model = EipFactory.eINSTANCE.createEIPModel();
      
      try {
         // Choose appropriate parser depending on file extension.
         // TODO There's probably a more robust strategy
         if (selectionFile.getName().endsWith(".xml")) {
            CamelXmlFileParser parser = new CamelXmlFileParser(
                  selectionFile.getLocation().toFile());
            parser.parseAndFillModel(model);
         } else if (selectionFile.getName().endsWith(".java")) {
            CamelJavaFileParser parser = new CamelJavaFileParser(
                  selectionFile.getLocation().toFile());
            parser.parseAndFillModel(model);
         }
      } catch (Exception e) {
         // TODO Manage parsing exception.
         e.printStackTrace();
         return null;
      }
      if (model.getOwnedRoutes() == null || model.getOwnedRoutes().isEmpty()) {
         // TODO Manage empty parsing result.
         System.err.println("No Route was found during Apache Camel file parsing !");
         return null;
      }
      return model.getOwnedRoutes().get(0);
   }
}
