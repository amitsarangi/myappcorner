
package me.amitsarangi.myappcorner.components;

import org.mt4j.sceneManagement.AbstractScene;

public interface SceneInterface
{
	public Class<? extends AbstractScene> getSceneClass();
	public String getSceneName();
}

