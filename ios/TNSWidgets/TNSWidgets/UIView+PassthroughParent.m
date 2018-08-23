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
    UIView *hitTestView = [self passThrough_hitTest:point withEvent:event]; // swizzled
    if (hitTestView == self && self.passthroughParent) {
        hitTestView = nil;
    }

    return hitTestView;
}

@end
