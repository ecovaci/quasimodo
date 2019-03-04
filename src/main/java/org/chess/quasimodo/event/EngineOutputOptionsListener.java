/*******************************************************************************
 * Quasimodo - a chess interface for playing and analyzing chess games.
 * Copyright (C) 2011 Eugen Covaci.
 * All rights reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 ******************************************************************************/
package org.chess.quasimodo.event;

import org.chess.quasimodo.Application;
import org.chess.quasimodo.engine.UCIKeywords;
import org.chess.quasimodo.engine.model.IdOptions;
import org.chess.quasimodo.engine.model.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EngineOutputOptionsListener implements EngineOutputListener {

    private IdOptions idOptions = new IdOptions();

    @Override
    public void onReceiveLine(String line) {
        if (line.startsWith(UCIKeywords.OPTION)) {
            String[] splited = line.split("\\s");
            Option option = new Option();
            int i = 2;
            option.name = splited[i];
            while (++i < splited.length && !UCIKeywords.TYPE.equals(splited[i])) {
                option.name += " " + splited[i];
            }
            option.type = splited[++i];
            while (++i < splited.length) {
                if (UCIKeywords.DEFAULT.equals(splited[i])) {
                    option.defaultValue = i < splited.length - 1 ? splited[++i] : "";
                    option.value = option.defaultValue;
                } else if (UCIKeywords.MIN.equals(splited[i])) {
                    option.min = splited[++i];
                } else if (UCIKeywords.MAX.equals(splited[i])) {
                    option.max = splited[++i];
                } else {
                    option.varLine += splited[i] + " ";
                }
            }
            if (!option.isTypeButton()) {
                idOptions.addOption(option);
            }
        } else if (line.startsWith(UCIKeywords.ID)) {
            String[] splited = line.split("\\s");
            if (splited.length > 2) {
                String value = "";
                for (int j = 2; j < splited.length; j++) {
                    value += splited[j] + " ";
                }
                idOptions.addId(splited[1], value.trim());
            }
        }
    }

    public IdOptions getIdOptions() {
        return idOptions;
    }

}
