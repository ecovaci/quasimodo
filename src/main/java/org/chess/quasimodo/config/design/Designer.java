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
package org.chess.quasimodo.config.design;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Properties;

import org.chess.quasimodo.annotation.Design;
import org.chess.quasimodo.errors.DesignException;
import org.chess.quasimodo.errors.FormatException;
import org.chess.quasimodo.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;


/**
 * Spring managed class for injecting design into designable classes, update and save the design configuration file.
 *
 * @author Eugen Covaci
 * @see {@link org.chess.quasimodo.config.design.Designable}
 */
@Component
public final class Designer implements ApplicationContextAware {
    private Logger logger = LoggerFactory.getLogger(Designer.class);

    @Autowired
    private ApplicationContext applicationContext;

    private String filepath;

    private Properties designSettings;

    /**
     * Constructor.
     *
     * @throws DesignException
     */
    public Designer() throws DesignException {
        //load settings
        try {
            String filepath = System.getProperty("user.home") + File.separator + ".quasimodo" + File.separator + "design.properties";
            logger.debug("Loading design properties from filepath: " + filepath);
            designSettings = PropertiesLoaderUtils.loadProperties(new FileSystemResource(filepath));
            this.filepath = filepath;
        } catch (IOException e) {
            throw new DesignException("Error on loading design's properties file", e);
        }
    }

    /**
     * Gets the value from configuration file.
     *
     * @param key The name of the property in configuration file
     * @param cls The class of the desired config value.
     * @return The configuration value.
     */
    public Object getValue(String key, Class<?> cls) {
        String value = designSettings.getProperty(key);
        logger.debug("Process key " + key + ", value " + value);
        if (StringUtils.hasLength(value)) {
            value = value.trim();
            if (cls == Font.class) {
                //<name>,<style>,<size>
                String[] splited = value.split(",");
                if (splited.length == 3) {
                    int style = 0;
                    for (String item : splited[1].split("|")) {
                        if (StringUtils.hasLength(item.trim())) {
                            style |= Integer.parseInt(item.trim());
                        }
                    }
                    return new Font(splited[0].trim(), style, Integer.parseInt(splited[2].trim()));
                } else {
                    throw new FormatException("Illegal font format [" + value + "] for key [" + key + "]");
                }
            } else if (cls == Color.class) {
                //RGB format
                String[] splited = value.split(",");
                if (splited.length == 3) {
                    return new Color(Integer.parseInt(splited[0].trim()),
                            Integer.parseInt(splited[1].trim()), Integer.parseInt(splited[2]));
                } else {
                    throw new FormatException("Illegal color format [" + value + "] for key [" + key + "]");
                }
            } else if (cls == Stroke.class) {
                String[] splited = value.split(",");
                if (splited.length == 3) {
                    return new BasicStroke(Float.parseFloat(splited[0].trim()),
                            Integer.parseInt(splited[1].trim()), Integer.parseInt(splited[2]));
                } else {
                    throw new FormatException("Illegal stroke format [" + value + "] for key [" + key + "]");
                }
            } else if (cls == Integer.class) {
                return new Integer(value);
            } else if (cls == Rectangle.class) {
                return Utils.toRectangle(value);
            } else {//default String
                return value;
            }
        }
        return null;
    }

    /**
     * Injects the configuration objects through the fields annotated with {@link org.chess.quasimodo.annotation.Design} of the <code>designable</code>
     * argument.
     *
     * @param designable The object to be designed.
     * @throws DesignException
     */
    public void injectDesign(Object designable) throws DesignException {
        try {
            Design annotation;
            for (Method method : designable.getClass().getDeclaredMethods()) {
                logger.debug("Method name [" + method.getName() + "]");

                annotation = method.getAnnotation(Design.class);
                if (annotation != null) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length == 0) {//only setters does matter, ignore getters
                        continue;
                    }

                    String[] keys = annotation.key();
                    Assert.isTrue(parameterTypes.length == keys.length,
                            "Number of arguments different from number of keys");
                    Object[] args = new Object[annotation.key().length];
                    for (int i = 0; i < keys.length; i++) {
                        args[i] = getValue(keys[i], parameterTypes[i]);
                    }
                    method.invoke(designable, args);
                    logger.debug("Method name [" + method.getName() + "] + invoked successfuly");
                }
            }
        } catch (Exception e) {
            throw new DesignException("Error on applying design", e);
        }

    }

    /**
     * Injects the configuration objects through the fields annotated with {@link org.chess.quasimodo.annotation.Design} of all designable
     * Spring beans (i.e. all Spring beans annotated with {@link org.chess.quasimodo.config.design.Designable}).
     *
     * @throws DesignException
     */
    public void injectDesign() throws DesignException {
        for (Object obj : applicationContext.getBeansOfType(Designable.class).values()) {
            injectDesign(obj);
            ((Designable) obj).refreshDesign();
            logger.debug("Design applied to: " + obj.getClass().getName());
        }
    }

    public void synchronizeDesign(Object designable) throws DesignException {
        try {
            Design annotation;
            for (Method method : designable.getClass().getDeclaredMethods()) {
                annotation = method.getAnnotation(Design.class);
                if (annotation != null && method.getParameterTypes().length == 0) {
                    if (annotation.key().length > 1) {
                        logger.warn("On syncronizing design it should have only one key, not: " + annotation.key().length);
                    }
                    Object obj = method.invoke(designable);
                    if (method.getReturnType() == Font.class) {
                        Font font = (Font) obj;
                        designSettings.setProperty(annotation.key()[0], font.getName() + "," + font.getStyle() + "," + font.getSize());
                    } else if (method.getReturnType() == Color.class) {
                        Color color = (Color) obj;
                        designSettings.setProperty(annotation.key()[0], color.getRed() + "," + color.getGreen() + "," + color.getBlue());
                    } else {
                        designSettings.setProperty(annotation.key()[0], obj != null ? obj.toString() : "");
                    }
                }
            }
        } catch (Exception e) {
            throw new DesignException("Error on saving design", e);
        }
    }

    public void synchronizeDesign() throws DesignException {
        for (Object obj : applicationContext.getBeansOfType(Designable.class).values()) {
            synchronizeDesign(obj);
        }

    }

    public void save() throws DesignException {
        OutputStream output = null;
        try {
            output = new FileOutputStream(filepath);
            designSettings.store(output, "Quasimodo design settings, modified on: " + new Date());
        } catch (Exception e) {
            throw new DesignException("Cannot store design settings", e);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }
}
