/*
 * QCRI, NADEEF LICENSE
 * NADEEF is an extensible, generalized and easy-to-deploy data cleaning platform built at QCRI.
 * NADEEF means “Clean” in Arabic
 *
 * Copyright (c) 2011-2013, Qatar Foundation for Education, Science and Community Development (on
 * behalf of Qatar Computing Research Institute) having its principle place of business in Doha,
 * Qatar with the registered address P.O box 5825 Doha, Qatar (hereinafter referred to as "QCRI")
 *
 * NADEEF has patent pending nevertheless the following is granted.
 * NADEEF is released under the terms of the MIT License, (http://opensource.org/licenses/MIT).
 */

package qa.qcri.nadeef.core.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import qa.qcri.nadeef.core.datamodel.Cell;
import qa.qcri.nadeef.core.datamodel.Column;
import qa.qcri.nadeef.core.datamodel.Fix;
import qa.qcri.nadeef.core.datamodel.NadeefConfiguration;
import qa.qcri.nadeef.tools.Tracer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

/**
 * Fix extension class.
 */
public class Fixes {
    /**
     * Generates fix id from database.
     * TODO: This doesn't promise that you will always get the right id in
     * concurrency mode, design a better way of generating it.
     * @return id.
     */
    public static int generateFixId() {
        Tracer tracer = Tracer.getTracer(Fix.class);
        try {
            String tableName = NadeefConfiguration.getRepairTableName();
            Connection conn = DBConnectionFactory.getNadeefConnection();
            Statement stat = conn.createStatement();
            ResultSet resultSet =
                stat.executeQuery("SELECT MAX(id) + 1 as id from " + tableName);
            conn.commit();
            int result = -1;
            if (resultSet.next()) {
                result = resultSet.getInt("id");
            }
            stat.close();
            conn.close();
            return result;
        } catch (Exception ex) {
            tracer.err("Unable to generate Fix id.", ex);
        }
        return 0;
    }

    public static Collection<Fix> fromQuery(ResultSet resultSet) {
        Preconditions.checkNotNull(resultSet);
        List<Fix> result = Lists.newArrayList();
        Cell.Builder cellBuilder = new Cell.Builder();
        try {
            while (resultSet.next()) {
                int vid = resultSet.getInt("vid");
                int op = resultSet.getInt("op");
                int c1TupleId = resultSet.getInt("c1_tupleid");
                Fix.Builder builder = new Fix.Builder();

                String c1TableName = resultSet.getString("c1_tablename");
                String c1Attribute = resultSet.getString("c1_attribute" );
                String c1Value = resultSet.getString("c1_value" );
                int c2TupleId = resultSet.getInt("c2_tupleid");
                String c2TableName = resultSet.getString("c2_tablename");
                String c2Attribute = resultSet.getString("c2_attribute");
                String c2Value = resultSet.getString("c2_value");
                Cell c1Cell =
                    cellBuilder.column(new Column(c1TableName, c1Attribute))
                        .value(c1Value)
                        .tid(c1TupleId)
                        .build();
                Fix newFix = null;
                // TODO: support different type of operations
                if (c2TableName != null) {
                    Cell c2Cell =
                        cellBuilder.column(
                            new Column(c2TableName, c2Attribute)
                        ).value(c2Value).tid(c2TupleId).build();
                    newFix = builder.vid(vid).left(c1Cell).right(c2Cell).build();
                } else {
                    newFix = builder.vid(vid).left(c1Cell).right(c2Value).build();
                }

                result.add(newFix);
            }
        } catch (SQLException e) {
            Tracer tracer = Tracer.getTracer(Fixes.class);
            tracer.err("Exceptions happen during parsing Fixes.", e);
        }
        return result;
    }
}
