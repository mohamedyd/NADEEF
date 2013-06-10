/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package qa.qcri.nadeef.test;

import qa.qcri.nadeef.core.datamodel.CleanPlan;
import qa.qcri.nadeef.core.exception.InvalidCleanPlanException;
import qa.qcri.nadeef.core.exception.InvalidRuleException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

/**
 * Factory class to get TestData.
 */
public class TestDataRepository {

    public static File getDumpTestCSVFile() {
        final String filePath = "test*src*qa*qcri*nadeef*test*input*dumptest.csv";
        return new File(filePath.replace('*', File.separatorChar));
    }

    public static File getPairCleanPlanFile1() {
        final String filePath = "test*src*qa*qcri*nadeef*test*input*PairTableCleanPlan1.json";
        return new File(filePath.replace('*', File.separatorChar));
    }

    public static File getFailurePlanFile1() {
        final String filePath = "test*src*qa*qcri*nadeef*test*input*FailurePlan1.json";
        return new File(filePath.replace('*', File.separatorChar));
    }

    public static File getFailurePlanFile2() {
        final String filePath = "test*src*qa*qcri*nadeef*test*input*FailurePlan2.json";
        return new File(filePath.replace('*', File.separatorChar));
    }

    public static File getTestFile1() {
        final String filePath = "test*src*qa*qcri*nadeef*test*input*CleanPlan1.json";
        return new File(filePath.replace('*', File.separatorChar));
    }

    public static File getTestFile2() {
        final String filePath = "test*src*qa*qcri*nadeef*test*input*CleanPlan2.json";
        return new File(filePath.replace('*', File.separatorChar));
    }

    public static File getTestFile3() {
        final String filePath = "test*src*qa*qcri*nadeef*test*input*CleanPlan3.json";
        return new File(filePath.replace('*', File.separatorChar));
    }

    public static File getTestFile4() {
        final String filePath = "test*src*qa*qcri*nadeef*test*input*CleanPlan4.json";
        return new File(filePath.replace('*', File.separatorChar));
    }

    public static File getTestFile5() {
        final String filePath = "test*src*qa*qcri*nadeef*test*input*CleanPlan5.json";
        return new File(filePath.replace('*', File.separatorChar));
    }

    public static File getViolationTestData1() {
        final String filePath = "test*src*qa*qcri*nadeef*test*input*violation1.csv";
        return new File(filePath.replace('*', File.separatorChar));
    }

    public static File getFixTestData1() {
        final String filePath = "test*src*qa*qcri*nadeef*test*input*Fix1.csv";
        return new File(filePath.replace('*', File.separatorChar));
    }

    public static File getLocationData1() {
        final String filePath = "test*src*qa*qcri*nadeef*test*input*locations.csv";
        return new File(filePath.replace('*', File.separatorChar));
    }

    public static File getAdult1kPlan() {
        final String filePath = "test*src*qa*qcri*nadeef*test*input*CleanPlan6.json";
        return new File(filePath.replace('*', File.separatorChar));
    }

    public static File getAdult30kPlan() {
        final String filePath = "test*src*qa*qcri*nadeef*test*input*CleanPlan7.json";
        return new File(filePath.replace('*', File.separatorChar));
    }

    public static File getStressPlan10kFile() {
        final String filePath = "test*src*qa*qcri*nadeef*test*input*StressPlan10k.json";
        return new File(filePath.replace('*', File.separatorChar));
    }

    public static File getStressPlan30kFile() {
        final String filePath = "test*src*qa*qcri*nadeef*test*input*StressPlan30k.json";
        return new File(filePath.replace('*', File.separatorChar));
    }

    public static File getStressPlan40kFile() {
        final String filePath = "test*src*qa*qcri*nadeef*test*input*StressPlan40k.json";
        return new File(filePath.replace('*', File.separatorChar));
    }

    public static File getStressPlan80kFile() {
        final String filePath = "test*src*qa*qcri*nadeef*test*input*StressPlan80k.json";
        return new File(filePath.replace('*', File.separatorChar));
    }

    public static File getStressPlan90kFile() {
        final String filePath = "test*src*qa*qcri*nadeef*test*input*StressPlan90k.json";
        return new File(filePath.replace('*', File.separatorChar));
    }

    public static File getStressPlan100kFile() {
        final String filePath = "test*src*qa*qcri*nadeef*test*input*StressPlan100k.json";
        return new File(filePath.replace('*', File.separatorChar));
    }

    public static CleanPlan getCleanPlan()
        throws
            InvalidRuleException,
            FileNotFoundException,
            InvalidCleanPlanException {
        return CleanPlan.createCleanPlanFromJSON(new FileReader(getTestFile1())).get(0);
    }

    public static CleanPlan getCleanPlan2()
        throws
        InvalidRuleException,
        FileNotFoundException,
        InvalidCleanPlanException {
        return CleanPlan.createCleanPlanFromJSON(new FileReader(getTestFile2())).get(0);
    }

    public static CleanPlan getCleanPlan3()
        throws
        InvalidRuleException,
        FileNotFoundException,
        InvalidCleanPlanException {
        return CleanPlan.createCleanPlanFromJSON(new FileReader(getTestFile3())).get(0);
    }

    public static CleanPlan getCleanPlan4()
        throws
        InvalidRuleException,
        FileNotFoundException,
        InvalidCleanPlanException {
        return CleanPlan.createCleanPlanFromJSON(new FileReader(getTestFile4())).get(0);
    }

    public static List<CleanPlan> getCleanPlan5()
        throws
        InvalidRuleException,
        FileNotFoundException,
        InvalidCleanPlanException {
        return CleanPlan.createCleanPlanFromJSON(new FileReader(getTestFile5()));
    }

    public static CleanPlan getAdultPlan1()
        throws
            InvalidRuleException,
            FileNotFoundException,
            InvalidCleanPlanException {
            return CleanPlan.createCleanPlanFromJSON(new FileReader(getAdult1kPlan())).get(0);
    }

    public static CleanPlan getAdultPlan2()
        throws
        InvalidRuleException,
        FileNotFoundException,
        InvalidCleanPlanException {
        return CleanPlan.createCleanPlanFromJSON(new FileReader(getAdult30kPlan())).get(0);
    }

    public static CleanPlan getStressPlan10k()
        throws
        InvalidRuleException,
        FileNotFoundException,
        InvalidCleanPlanException {
        return CleanPlan.createCleanPlanFromJSON(new FileReader(getStressPlan10kFile())).get(0);
    }

    public static CleanPlan getStressPlan30k()
        throws
        InvalidRuleException,
        FileNotFoundException,
        InvalidCleanPlanException {
        return CleanPlan.createCleanPlanFromJSON(new FileReader(getStressPlan30kFile())).get(0);
    }

    public static CleanPlan getStressPlan40k()
        throws
        InvalidRuleException,
        FileNotFoundException,
        InvalidCleanPlanException {
        return CleanPlan.createCleanPlanFromJSON(new FileReader(getStressPlan40kFile())).get(0);
    }

    public static CleanPlan getStressPlan80k()
        throws
        InvalidRuleException,
        FileNotFoundException,
        InvalidCleanPlanException {
        return CleanPlan.createCleanPlanFromJSON(new FileReader(getStressPlan80kFile())).get(0);
    }

    public static CleanPlan getStressPlan90k()
        throws
        InvalidRuleException,
        FileNotFoundException,
        InvalidCleanPlanException {
        return CleanPlan.createCleanPlanFromJSON(new FileReader(getStressPlan90kFile())).get(0);
    }

    public static CleanPlan getPairCleanPlan1()
        throws
            InvalidRuleException,
            FileNotFoundException,
            InvalidCleanPlanException {
        return CleanPlan.createCleanPlanFromJSON(new FileReader(getPairCleanPlanFile1())).get(0);
    }
}
