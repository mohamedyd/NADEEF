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

package qa.qcri.nadeef.test.core;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import qa.qcri.nadeef.core.datamodel.CleanPlan;
import qa.qcri.nadeef.core.datamodel.NadeefConfiguration;
import qa.qcri.nadeef.core.exception.InvalidCleanPlanException;
import qa.qcri.nadeef.core.exception.InvalidRuleException;
import qa.qcri.nadeef.core.util.Bootstrap;
import qa.qcri.nadeef.core.util.DBConnectionFactory;
import qa.qcri.nadeef.tools.CSVDumper;
import qa.qcri.nadeef.tools.DBConfig;
import qa.qcri.nadeef.core.datamodel.Rule;
import qa.qcri.nadeef.test.TestDataRepository;
import qa.qcri.nadeef.tools.SQLDialect;
import qa.qcri.nadeef.tools.Tracer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.util.List;

/**
 * CleanPlan unit test
 */
@RunWith(JUnit4.class)
public class CleanPlanTest {
    private ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        Bootstrap.start();
        Tracer.setVerbose(true);

        Connection conn = null;
        try {
            conn = DBConnectionFactory.getNadeefConnection();
            CSVDumper.dump(conn, TestDataRepository.getLocationData1(), "location");
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail(ex.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception ex) {
                    // ignore
                }
            }
        }
    }

    @After
    public void teardown() {
        Bootstrap.shutdown();
    }

    @Test
    public void createFromJSONTest() {
        try {
            List<CleanPlan> cleanPlans =
                CleanPlan.createCleanPlanFromJSON(
                    new FileReader(TestDataRepository.getTestFile1())
                );

            Assert.assertEquals(1, cleanPlans.size());
            CleanPlan cleanPlan = cleanPlans.get(0);

            DBConfig source = cleanPlan.getSourceDBConfig();
            Assert.assertEquals("jdbc:postgresql://localhost/unittest", source.getUrl());
            Assert.assertEquals("tester", source.getUserName());
            Assert.assertEquals("tester", source.getPassword());
            Assert.assertEquals(SQLDialect.POSTGRES, source.getDialect());
            Rule rule = cleanPlan.getRule();
            List<String> tableNames = rule.getTableNames();
            Assert.assertEquals(1, tableNames.size());
            Assert.assertEquals("location_copy", tableNames.get(0));
        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        }
    }

    @Test
    public void createFromJSONTest2() {
        thrown.expect(IllegalArgumentException.class);
        try {
            CleanPlan.createCleanPlanFromJSON(
                new FileReader(TestDataRepository.getFailurePlanFile1())
            );
        } catch (InvalidCleanPlanException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidRuleException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createFromJSONTest3() {
        thrown.expect(InvalidRuleException.class);
        try {
            CleanPlan.createCleanPlanFromJSON(
                new FileReader(TestDataRepository.getFailurePlanFile2())
            );
        } catch (InvalidCleanPlanException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidRuleException e) {
            e.printStackTrace();
        }
    }
}
