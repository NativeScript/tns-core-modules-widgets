//
//  UIView+PassthroughParent.m
//  TNSWidgets
//
//  Created by Manol Donev on 21.08.18.
//  Copyright © 2018 Telerik A D. All rights reserved.
//

#import "UIView+PassthroughParent.h"
#import "NSObject+Swizzling.h"
#import "NSObject+PropertyBag.h"


NSString * const TLKPassthroughParentKey = @"passthroughParent";

@implementation UIView (PassthroughParent)

+ (void) load {
    [self loadHitTest];
}

+ (void) loadHitTest {
    @autoreleasepool {
        static dispatch_once_t onceToken;
        dispatch_once(&onceToken, ^{
            [self swizzleInstanceMethodWithOriginalSelector:@selector(hitTest:withEvent:) fromClass:self.class withSwizzlingSelector:@selector(passThrough_hitTest:withEvent:)];
        });
    }
}

- (BOOL)passthroughParent {
    NSNumber *passthrough = [self propertyValueForKey:TLKPassthroughParentKey];
    if (passthrough) {
        return passthrough.boolValue;
    };

    return NO;
}

- (void)setPassthroughParent:(BOOL)passthroughParent {
    [self setPropertyValue:[NSNumber numberWithBool:passthroughParent] forKey:TLKPassthroughParentKey];
}

- (UIView *)passThrough_hitTest:(CGPoint)point withEvent:(UIEvent *)event {
    if (self.isUserInteractionEnabled || !self.passthroughParent || self.isHidden || self.alpha <= 0.01) {
        return [self passThrough_hitTest:point withEvent:event]; // swizzled
    }

    if ([self pointInside:point withEvent:event]) {
        for (UIView *subview in [self.subviews reverseObjectEnumerator]) {
            CGPoint convertedPoint = [subview convertPoint:point fromView:self];
            UIView *hitTestView = [subview hitTest:convertedPoint withEvent:event];
            if (hitTestView) {
                return hitTestView;
            }
        }
    }
    
    return nil;
}

@end
