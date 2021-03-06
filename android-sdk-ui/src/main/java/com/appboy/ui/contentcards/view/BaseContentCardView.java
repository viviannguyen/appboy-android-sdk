package com.appboy.ui.contentcards.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appboy.models.cards.Card;
import com.appboy.ui.actions.IAction;
import com.appboy.ui.actions.UriAction;
import com.appboy.ui.contentcards.AppboyContentCardsManager;
import com.appboy.ui.widget.BaseCardView;

/**
 * Base class for ContentCard views
 */
public abstract class BaseContentCardView<T extends Card> extends BaseCardView<T> {
  public BaseContentCardView(Context context) {
    super(context);
  }

  public abstract ContentCardViewHolder createViewHolder(ViewGroup viewGroup);

  public void bindViewHolder(ContentCardViewHolder viewHolder, final T card) {
    viewHolder.setPinnedIconVisible(card.getIsPinned());
    viewHolder.setUnreadBarVisible(mAppboyConfigurationProvider.isContentCardsUnreadVisualIndicatorEnabled() && !card.isIndicatorHighlighted());
    final UriAction mCardAction = getUriActionForCard(card);
    viewHolder.itemView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        handleCardClick(mContext, card, mCardAction, getClassLogTag());
      }
    });

    // Only set the action hint to visible if there's a card action
    viewHolder.setActionHintVisible(mCardAction != null);
  }

  /**
   * Sets the card's image to a given url. The view may be null.
   *
   * @param imageView The ImageView
   * @param cardAspectRatio The aspect ratio as set by the card itself
   * @param cardImageUrl The image url
   * @param defaultAspectRatio The default aspect ratio if the cardAspectRatio is 0
   */
  public void setOptionalCardImage(@Nullable ImageView imageView, float cardAspectRatio,
                                   String cardImageUrl, float defaultAspectRatio) {
    float aspectRatio = defaultAspectRatio;

    if (cardAspectRatio != 0f) {
      aspectRatio = cardAspectRatio;
    }

    if (imageView != null) {
      setImageViewToUrl(imageView, cardImageUrl, aspectRatio);
    }
  }

  @Override
  protected boolean isClickHandled(Context context, Card card, IAction cardAction) {
    return AppboyContentCardsManager.getInstance().getContentCardsActionListener().onContentCardClicked(context, card, cardAction);
  }

  @TargetApi(21)
  protected void safeSetClipToOutline(ImageView imageView) {
    if (imageView != null) {
      imageView.setClipToOutline(true);
    }
  }
}
