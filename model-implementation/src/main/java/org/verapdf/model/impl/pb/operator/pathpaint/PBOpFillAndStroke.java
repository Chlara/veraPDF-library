package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;

import java.util.List;

/**
 * Base class for path paint operators, which stroke and fill path
 *
 * @author Evgeniy Muravitskiy
 */
public abstract class PBOpFillAndStroke extends PBOpPathPaint {

	protected PBOpFillAndStroke(List<COSBase> arguments, PDAbstractPattern pattern,
			PDColorSpace pbStrokeColorSpace, PDColorSpace pbFillColorSpace,
			final String opType) {
		super(arguments, pattern, pbStrokeColorSpace, pbFillColorSpace, opType);
	}

	@Override
	public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(
			String link) {
		switch (link) {
			case STROKE_CS:
				return this.getStrokeCS();
			case FILL_CS:
				return this.getFillCS();
			default:
				return super.getLinkedObjects(link);
		}
	}

}
