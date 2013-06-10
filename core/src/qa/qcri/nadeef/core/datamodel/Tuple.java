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

package qa.qcri.nadeef.core.datamodel;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Tuple class represents a tuple (row) in a table.
 */
public class Tuple {
    //<editor-fold desc="Private Fields">
    private Object[] values;
    private Schema schema;
    private String tableName;
    private int tupleId;
    //</editor-fold>

    //<editor-fold desc="Public Members">
    /**
     * Constructor.
     */
    public Tuple(int tupleId, Schema schema, Object[] values) {
        if (schema == null || values == null || tupleId < 1) {
            throw new IllegalArgumentException("Input attribute/value cannot be null.");
        }
        if (schema.size() != values.length) {
            throw new IllegalArgumentException("Incorrect input with attributes and values");
        }

        this.tableName = schema.getTableName();
        this.tupleId = tupleId;
        this.schema = schema;
        this.values = values;
    }

    /**
     * Gets the value from the tuple.
     * @param key The attribute key
     * @return Output Value
     */
    public Object get(Column key) {
        int index = schema.get(key);
        return values[index];
    }

    /**
     * Gets the value from the tuple.
     * @param columnAttribute The attribute key
     * @return Output Value
     */
    public Object get(String columnAttribute) {
        Column column = new Column(tableName, columnAttribute);
        int index = schema.get(column);
        return values[index];
    }

    /**
     * Gets the value from the tuple.
     * @param key The attribute key
     * @return Output Value
     */
    public String getString(Column key) {
        Object value = get(key);
        return (String)value;
    }

    /**
     * Gets the value from the tuple.
     * @param columnAttribute The attribute key
     * @return Output Value
     */
    public String getString(String columnAttribute) {
        Object value = get(columnAttribute);
        return (String)value;
    }

    /**
     * Gets Tuple Id.
     * @return tuple id.
     */
    public int getTupleId() {
        return this.tupleId;
    }

    /**
     * Gets the Cell given a column key.
     * @param key key.
     * @return Cell.
     */
    public Cell getCell(Column key) {
        return new Cell(key, tupleId, get(key));
    }

    /**
     * Gets the Cell given a column key.
     * @param key key.
     * @return Cell.
     */
    public Cell getCell(String key) {
        return new Cell(new Column(tableName, key), tupleId, get(key));
    }

    /**
     * Gets all the values in the tuple.
     * @return value collections.
     */
    public ImmutableSet<Cell> getCells() {
        List<Column> columns = schema.getColumns().asList();
        List<Cell> cells = Lists.newArrayList();
        for (Column column : columns) {
            if (column.getColumnName().equals("tid")) {
                continue;
            }
            Cell cell = new Cell(column, tupleId, get(column));
            cells.add(cell);
        }
        return ImmutableSet.copyOf(cells);
    }

    /**
     * Gets all the cells in the tuple.
     * @return Attribute collection
     */
    public Schema getSchema() {
        return schema;
    }

    /**
     * Returns <code>True</code> when the tuple is from the given table name.
     * @param tableName table name.
     * @return <code>True</code> when the tuple is from the given table name.
     */
    public boolean isFromTable(String tableName) {
        if (this.tableName.equalsIgnoreCase(tableName)) {
            return true;
        }

        if (this.tableName.startsWith("csv_")) {
            String originalTableName = this.tableName.substring(4);
            return originalTableName.equalsIgnoreCase(tableName);
        }
        return false;
    }

    /**
     * Returns <code>True</code> when given a tuple from the same schema, the values are
     * also the same. There is no check on the schema but only do a check on the values.
     * This is mainly used for optimization on tuple compare from the same schema.
     * @param tuple
     * @return <code>True</code> when the given tuple from the same schema also has the same
     * values.
     */
    public boolean hasSameValue(Tuple tuple) {
        if (tuple == null) {
            return false;
        }

        if (this == tuple || this.values == tuple.values) {
            return true;
        }

        if (values.length != tuple.values.length) {
            return false;
        }

        Optional<Integer> tidIndex = schema.getTidIndex();
        if (tidIndex.isPresent()) {
            // it returns true when the tid is the same.
            int tid = tidIndex.get();
            if (values[tid].equals(tuple.values[tid])) {
                return true;
            }
        }

        for (int i = 0; i < values.length; i ++) {
            // skip the TID compare
            if (tidIndex.isPresent() && i == tidIndex.get()) {
                continue;
            }

            if (values[i] == tuple.values[i]) {
                continue;
            }
            if (!values[i].equals(tuple.values[i])) {
                return false;
            }
        }
        return true;
    }
    //</editor-fold>
}
