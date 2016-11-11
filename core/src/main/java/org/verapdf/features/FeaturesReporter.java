package org.verapdf.features;

import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.tools.FeatureTreeNode;

import java.util.*;

/**
 * Features reporter
 *
 * @author Maksim Bezrukov
 */
public class FeaturesReporter {
	public static final String CUSTOM_FEATURES_NAME = "customFeatures";
	public static final String PLUGIN_FEATURES_NAME = "pluginFeatures";

	private static Map<FeatureObjectType, List<AbstractFeaturesExtractor>> featuresExtractors = new HashMap<>();

	private final FeatureExtractionResult collection;
	private final FeatureExtractorConfig config;

	/**
	 * Creates new FeaturesReporter
	 */
	public FeaturesReporter(FeatureExtractorConfig config, List<AbstractFeaturesExtractor> extractors) {
		if (extractors == null) {
			throw new IllegalArgumentException(nullMessage("extractors"));
		}
		if (config == null) {
			throw new IllegalArgumentException(nullMessage("config"));
		}
		this.config = config;
		this.collection = new FeatureExtractionResult();
		for (AbstractFeaturesExtractor extractor : extractors) {
			registerFeaturesExtractor(extractor);
		}
	}

	public FeaturesReporter(FeatureExtractorConfig config) {
		this(config, Collections.<AbstractFeaturesExtractor>emptyList());
	}

	/**
	 * Registers feature extractor for features object type
	 *
	 * @param extractor object for extract custom features
	 */
	static void registerFeaturesExtractor(AbstractFeaturesExtractor extractor) {
		if (featuresExtractors.get(extractor.getType()) == null) {
			featuresExtractors.put(extractor.getType(), new ArrayList<AbstractFeaturesExtractor>());
		}

		featuresExtractors.get(extractor.getType()).add(extractor);
	}

	/**
	 * Reports feature object for feature report
	 *
	 * @param obj object for reporting
	 */
	public void report(IFeaturesObject obj) {
		if (!config.isFeatureEnabled(obj.getType()))
			return;

		try {
			FeatureTreeNode root = obj.reportFeatures(this.collection);
			if (featuresExtractors.containsKey(obj.getType())) {
				FeaturesData objData = obj.getData();
				if (objData != null) {
					FeatureTreeNode custom = root.addChild(CUSTOM_FEATURES_NAME);
					for (AbstractFeaturesExtractor ext : featuresExtractors.get(obj.getType())) {
						List<FeatureTreeNode> cust = ext.getFeatures(objData);
						if (cust != null && !cust.isEmpty()) {
							FeatureTreeNode custRoot = custom.addChild(PLUGIN_FEATURES_NAME);
							AbstractFeaturesExtractor.ExtractorDetails details = ext.getDetails();
							if (!details.getName().isEmpty()) {
								custRoot.setAttribute("name", details.getName());
							}
							if (!details.getVersion().isEmpty()) {
								custRoot.setAttribute("version", details.getVersion());
							}
							if (!details.getDescription().isEmpty()) {
								custRoot.setAttribute("description", details.getDescription());
							}
							for (FeatureTreeNode ftn : cust) {
								if (ftn != null) {
									custRoot.addChild(ftn);
								}
							}
						}
					}
				}
			}

		} catch (FeatureParsingException ignore) {
			// The method logic should ensure this never happens, so if it does
			// it's catastrophic. We'll throw an IllegalStateException with this
			// as a cause. The only time it's ignored is when the unthinkable
			// happens
			throw new IllegalStateException(
					"FeaturesReporter.report() illegal state.", ignore);
			// This exception occurs when wrong node creates for feature tree.
			// The logic of the method guarantees this doesn't occur.
		}
	}

	/**
	 * @return collection of featurereport
	 */
	public FeatureExtractionResult getCollection() {
		return this.collection;
	}

	private static String nullMessage(final String name) {
		return String.format("Argument %s cannot be null.", name);
	}

}
