/*
 * Copyright © 2013 Turkcell Teknoloji Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ttech.cordovabuild.domain.asset;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.core.support.AbstractLobStreamingResultSetExtractor;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.stereotype.Repository;

@Repository
public class AssetServiceImpl implements AssetService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetServiceImpl.class);
    private final String DDL = "CREATE TABLE ASSETS\n" +
            "(\n" +
            "    UUID VARCHAR(36) PRIMARY KEY NOT NULL,\n" +
            "    DATA LONGVARBINARY NOT NULL\n" +
            ");\n";
    private JdbcTemplate jdbcTemplate;
    private DefaultLobHandler lobHandler = new DefaultLobHandler();

    @PostConstruct
    public void checkExistence() {
        try {
            jdbcTemplate.execute("select * from ASSETS limit 1");
        } catch (BadSqlGrammarException e) {
            LOGGER.info("could not found table ASSETS creating");
            jdbcTemplate.execute(DDL);
        }
    }

    @Override
    public void handleInputStream(AssetRef assetRef, final InputStreamHandler handler) {
        jdbcTemplate.query("select data from ASSETS where UUID=?", new AbstractLobStreamingResultSetExtractor() {
            @Override
            protected void streamData(ResultSet rs) throws SQLException, IOException, DataAccessException {
                handler.handleInputStream(lobHandler.getBlobAsBinaryStream(rs, 1));
            }
        }, assetRef.getUuid());
    }

    @Override
    public AssetRef save(final InputStream inputStream) {
        final AssetRef ref=new AssetRef();
        jdbcTemplate.execute("insert into ASSETS (uuid,data) values (?,?)", new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
            @Override
            protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException, DataAccessException {
                ref.setUuid(UUID.randomUUID().toString());
                ps.setString(1, ref.getUuid());
                try {
                    lobCreator.setBlobAsBytes(ps,2, ByteStreams.toByteArray(inputStream));
                } catch (IOException e) {
                    throw new AssetCreationException(e);
                }
            }
        });
        return ref;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

}
